package com.example.moneytalks.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytalks.APISetup.RetrofitClient
import com.example.moneytalks.DataClasses.Notification
import com.example.moneytalks.ui.theme.gradient
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
                retrofitClient.api.acceptInvite(userId = notification.userId, groupId = notification.groupId)

                notifications.remove(notification)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun declineInvite(notification: Notification){
        viewModelScope.launch {
            try{
                retrofitClient.api.declineInvite(notification.id)

                notifications.remove(notification)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}