package com.example.moneytalks.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytalks.apisetup.RetrofitClient
import com.example.moneytalks.dataclasses.LoginRequest
import com.example.moneytalks.dataclasses.LoginResponse
import com.example.moneytalks.dataclasses.SignupRequest
import com.example.moneytalks.dataclasses.SignupResponse
import com.example.moneytalks.dataclasses.User
import kotlinx.coroutines.launch

class UserViewModel(private val retrofitClient: RetrofitClient = RetrofitClient): ViewModel() {
    val currentUser = mutableStateOf<User?>(null)
    val errorMessage = mutableStateOf<String?>(null)
    val isLoading = mutableStateOf(false)

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
    fun login(username: String, password: String) {
        println("Attempting login with username=$username password=$password") // Debug
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            try {
                // Call backend
                val response: LoginResponse = retrofitClient.api.login(
                    LoginRequest(username, password)
                )
                println("Login response: $response") // Debug

                // Handle success/failure
                if (response.success && response.user != null) {
                    currentUser.value = response.user
                } else {
                    errorMessage.value = response.message ?: "Login failed"
                }
            } catch (e: Exception) {
                e.printStackTrace() // <-- make sure errors are logged
                errorMessage.value = "Login request failed: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }


    fun signup(username: String, fullName: String, email: String, password: String) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            try {
                val response: SignupResponse = retrofitClient.api.signup(
                    SignupRequest(username, fullName, email, password)
                )

                if (response.success && response.user != null) {
                    // Auto-login after signup
                    val loginResponse: LoginResponse = retrofitClient.api.login(
                        LoginRequest(username, password)
                    )

                    if (loginResponse.success && loginResponse.user != null) {
                        currentUser.value = loginResponse.user
                    } else {
                        errorMessage.value = loginResponse.message ?: "Auto-login failed"
                    }
                } else {
                    errorMessage.value = response.message ?: "Signup failed"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = "Signup request failed: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }
}