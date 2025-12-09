package com.example.moneytalks.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import retrofit2.HttpException
import com.example.moneytalks.apisetup.RetrofitClient
import com.example.moneytalks.dataclasses.Expense
import com.example.moneytalks.workers.ExpenseSyncWorker
import com.example.moneytalks.dataclasses.GroupMember
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.concurrent.TimeUnit

class ExpenseViewModel(private val retrofitClient: RetrofitClient = RetrofitClient): ViewModel() {

    var expenseHistory = mutableStateOf<List<Expense>>(emptyList())

    fun createExpense(
        context: Context,
        userId: String,
        memberId: String,
        groupId: String,
        amount: Double,
        description: String,
        action: String,
        payers: List<GroupMember>,
        onSuccess: () -> Unit,
        onNetworkRetryScheduled: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch {
            try {
                val expense = Expense(
                    groupId = groupId,
                    userId = userId,
                    memberId = memberId,
                    amount = amount,
                    description = description,
                    action = action,
                    payers = payers.map { it.id }
                )
                retrofitClient.api.createExpense(userId, expense)

                onSuccess()

            } catch (e: IOException) {
                e.printStackTrace()
                scheduleExpenseRetry(context, userId, memberId, groupId, amount, description, payers)
                onNetworkRetryScheduled()
            } catch(e: HttpException){
                println(e.message)
                println(e.response()?.errorBody().toString())
                onError("Server error while creating expense")

            } catch (e: Exception){
                e.printStackTrace()
                println(e.message)
            }
        }
    }

    private fun scheduleExpenseRetry(
        context: Context,
        userId: String,
        memberId: String,
        groupId: String,
        amount: Double,
        description: String,
        payers: List<GroupMember>
    ){
        val gson = Gson()
        val payersJson = gson.toJson(payers)

        val workData = workDataOf(
            "userId" to userId,
            "memberId" to memberId,
            "groupId" to groupId,
            "amount" to amount,
            "description" to description,
            "payersJson" to payersJson
        )

        val request = OneTimeWorkRequestBuilder<ExpenseSyncWorker>()
            .setInputData(workData)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                TimeUnit.SECONDS.toMillis(30),
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }

    fun getExpenseHistoryByGroupId(groupId: String) {
        viewModelScope.launch {
            try {
                val response = retrofitClient.api.getExpenseHistory(groupId)
                expenseHistory.value = response
            } catch (e: HttpException) {
                println(e.message)
                println(e.response()?.errorBody().toString())

            } catch (e: Exception) {
                e.printStackTrace()
                println(e.message)
            }
        }
    }
}