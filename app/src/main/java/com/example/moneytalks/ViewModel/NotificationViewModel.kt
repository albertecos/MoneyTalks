package com.example.moneytalks.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytalks.APISetup.RetrofitClient
import com.example.moneytalks.DataClasses.Notification
import kotlinx.coroutines.launch

class NotificationViewModel(private val retrofitClient: RetrofitClient = RetrofitClient): ViewModel() {
    var notifications = mutableStateListOf<Notification>()

    fun fetchNotifications(userId: String) {
        notifications.clear()
        viewModelScope.launch{
            try{
                val response = retrofitClient.api.getNotifications(userId)
                notifications.addAll(response)
            }catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun acceptInvite(notification: Notification){
        viewModelScope.launch {
            try{
                retrofitClient.api.acceptInvite(notification.userId)

                notifications.remove(notification)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun declineInvite(notification: Notification){
        viewModelScope.launch {
            try{
                retrofitClient.api.declineInvite(notification.userId)

                notifications.remove(notification)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}