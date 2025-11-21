package com.example.moneytalks.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytalks.apisetup.RetrofitClient
import com.example.moneytalks.dataclasses.Notification
import kotlinx.coroutines.launch

class NotificationViewModel(private val retrofitClient: RetrofitClient = RetrofitClient): ViewModel() {
    var notifications = mutableStateListOf<Notification>()
    val notificationsEnabled = mutableStateOf(true)

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

    fun enableNotifications(userId: String) {
        viewModelScope.launch {
            try {
                retrofitClient.api.enableNotifications(userId)
                notificationsEnabled.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun disableNotifications(userId: String) {
        viewModelScope.launch {
            try {
                retrofitClient.api.disableNotifications(userId)
                notificationsEnabled.value = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
