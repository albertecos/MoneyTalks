package com.example.moneytalks.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.moneytalks.apisetup.RetrofitClient
import com.example.moneytalks.dataclasses.User

class UserViewModel(private val retrofitClient: RetrofitClient = RetrofitClient): ViewModel() {
    var currentUser = mutableStateOf<com.example.moneytalks.dataclasses.User?>(null)

    suspend fun searchUsers(query: String): List<User>? {
        return try {
            retrofitClient.api.searchUsers(query)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}