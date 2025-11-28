package com.example.moneytalks.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import retrofit2.HttpException
import com.example.moneytalks.apisetup.RetrofitClient
import com.example.moneytalks.dataclasses.Expense
import com.example.moneytalks.dataclasses.GroupMember
import kotlinx.coroutines.launch

class ExpenseViewModel(private val retrofitClient: RetrofitClient = RetrofitClient): ViewModel() {

    var expenseHistory = mutableStateOf<List<Expense>>(emptyList())

    fun createExpense(
        userId: String,
        groupId: String,
        amount: Double,
        description: String,
        action: String,
        payers: List<GroupMember>
    ){
        viewModelScope.launch {
            try {
                val expense = Expense(
                    groupId = groupId,
                    userId = userId,
                    amount = amount,
                    description = description,
                    action = action,
                    payers = payers
                )
                retrofitClient.api.createExpense(userId, expense)

            } catch(e: HttpException){
                println(e.message)
                println(e.response()?.errorBody().toString())


            }catch (e: Exception){
                e.printStackTrace()
                println(e.message)
            }
        }
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