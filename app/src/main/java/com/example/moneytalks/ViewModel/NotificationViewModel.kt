package com.example.moneytalks.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytalks.APISetup.RetrofitClient
import com.example.moneytalks.DataClasses.Notification
import kotlinx.coroutines.launch

class ExpenseNotificationViewModel(private val retrofitClient: RetrofitClient = RetrofitClient): ViewModel() {
    var expenseNotifications = mutableStateListOf<Notification>()

    fun fetchExpenseNotification(memberId: String) {
        expenseNotifications.clear()
        viewModelScope.launch{
            try{
                val response = retrofitClient.api.getNotifications(memberId)
                expenseNotifications.addAll(response)
            }catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}