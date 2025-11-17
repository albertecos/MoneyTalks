package com.example.moneytalks.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytalks.APISetup.RetrofitClient
import com.example.moneytalks.DataClasses.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.async

class UserViewModel(private val retrofitClient: RetrofitClient = RetrofitClient): ViewModel() {
    var currentUser = mutableStateOf<com.example.moneytalks.DataClasses.User?>(null)

    suspend fun searchUsers(query: String): List<User>? {
        return try {
            retrofitClient.api.searchUsers(query)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}