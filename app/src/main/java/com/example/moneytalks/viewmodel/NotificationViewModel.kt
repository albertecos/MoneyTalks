package com.example.moneytalks.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.moneytalks.apisetup.RetrofitClient
import com.example.moneytalks.dataclasses.Notification
import com.example.moneytalks.dataclasses.NotificationCreate
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
    
    fun createNotification(
        userId: String,
        action: String,
        groupId: String,
        groupName: String,
        amount: Double?,
        description: String
    ){
        viewModelScope.launch { 
            try {
                val notification = NotificationCreate(
                    action = action,
                    groupId = groupId,
                    groupName = groupName,
                    userId = userId,
                    amount = amount,
                    description = description,
                )

                retrofitClient.api.createNotification(userId, notification)
            }catch (e: HttpException) {
                e.printStackTrace()
                println("HTTP ERROR: ${e.message}")
            }catch(e: Exception){
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