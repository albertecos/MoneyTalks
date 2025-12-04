package com.example.moneytalks.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import retrofit2.HttpException
import com.example.moneytalks.apisetup.RetrofitClient
import com.example.moneytalks.dataclasses.Expense
import com.example.moneytalks.dataclasses.GroupMember
import com.example.moneytalks.utilityclasses.NotificationUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class ExpenseSyncWorker(
    appContext: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(appContext, workerParameters) {

    private val retrofitClient: RetrofitClient = RetrofitClient
    override suspend fun doWork(): Result {
        val userId = inputData.getString("userId") ?: return Result.failure()
        val groupId = inputData.getString("groupId") ?: return Result.failure()
        val memberId = inputData.getString("memberId") ?: return Result.failure()
        val description = inputData.getString("description") ?: return Result.failure()
        val amount = inputData.getDouble("amount", -1.0)
        val payersJson = inputData.getString("payersJson") ?: return Result.failure()

        if (amount <= 0.0) return Result.failure()

        val gson = Gson()
        val type = object : TypeToken<List<GroupMember>>() {}.type
        val payers: List<GroupMember> = gson.fromJson(payersJson, type)

        val expense = Expense(
            userId = userId,
            memberId = memberId,
            groupId = groupId,
            amount = amount,
            description = description,
            action = "expense",
            payers = payers.map { it.id }
        )

        return try {
            retrofitClient.api.createExpense(userId, expense)

            NotificationUtil.sendNotification(
                applicationContext,
                "Expense sent",
                "Your expense \"$description\" for $amount,- was saved."
            )
            Result.success()
        } catch (e: IOException){
            Result.retry()
        } catch (e: HttpException){
            Result.failure()
        } catch (e: Exception){
            Result.failure()
        }
    }
}