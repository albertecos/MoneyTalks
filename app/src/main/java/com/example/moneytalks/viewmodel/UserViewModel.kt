package com.example.moneytalks.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytalks.apisetup.RetrofitClient
import com.example.moneytalks.dataclasses.User
import kotlinx.coroutines.launch

class UserViewModel(private val retrofitClient: RetrofitClient = RetrofitClient): ViewModel() {
    var currentUser = mutableStateOf<com.example.moneytalks.dataclasses.User?>(null)

    init {
        // TEMP FIX: For testing the SettingsPage. Remove this and fix the login flow :(.
        currentUser.value = User(id = "c4d21a74-c59c-4a4b-8dea-9eb519428543", username = "testuser", email = "test@example.com", password = "password", full_name = "Test User", profile_picture = "")
    }

    suspend fun searchUsers(query: String): List<User>? {
        return try {
            retrofitClient.api.searchUsers(query)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun updateEmail(newEmail: String) {
        viewModelScope.launch {
            currentUser.value?.let {
                try {
                    val updatedUser = it.copy(email = newEmail)
                    currentUser.value = retrofitClient.api.updateUser(updatedUser)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun updatePassword(newPassword: String) {
        viewModelScope.launch {
            currentUser.value?.let {
                try {
                    val updatedUser = it.copy(password = newPassword)
                    currentUser.value = retrofitClient.api.updateUser(updatedUser)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
