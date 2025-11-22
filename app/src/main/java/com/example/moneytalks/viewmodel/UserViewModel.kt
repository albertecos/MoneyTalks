package com.example.moneytalks.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.moneytalks.apisetup.RetrofitClient
import com.example.moneytalks.dataclasses.User

class UserViewModel(private val retrofitClient: RetrofitClient = RetrofitClient): ViewModel() {
    var currentUser = mutableStateOf<User?>(null)

    //temporary to make stuff work. currentUser should be set when logged in
    val currentUserId = "c4d21a74-c59c-4a4b-8dea-9eb519428543"

    suspend fun searchUsers(query: String): List<User>? {
        return try {
            retrofitClient.api.searchUsers(query)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun logout(){
        currentUser.value = null
    }
}