package com.example.moneytalks.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytalks.apisetup.RetrofitClient
import com.example.moneytalks.dataclasses.LoginRequest
import com.example.moneytalks.dataclasses.User
import com.example.moneytalks.dataclasses.UserCreate
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UserViewModel(private val retrofitClient: RetrofitClient = RetrofitClient) : ViewModel() {
    var currentUser = mutableStateOf<User?>(null)

    suspend fun searchUsers(query: String): List<User>? {
        return try {
            retrofitClient.api.searchUsers(query)
        }catch(e: HttpException){
            println("HTTP error in UserViewModel searchUsers: ${e.message}")
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error in searchUsers in UserViewModel")
            null
        }
    }

    fun signup(
        username: String,
        profile_picture: String,
        fullName: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val user = UserCreate(
                    username = username,
                    profile_picture = profile_picture,
                    full_name = fullName,
                    email = email,
                    password = password,
                )
                val current = retrofitClient.api.signup(user)
                currentUser.value = current
                onSuccess()
            } catch (e: HttpException) {
                println(e.message)
                println(e.stackTrace)
                onError(e)
            } catch (e: Exception) {
                e.printStackTrace()
                onError(e)
            }
        }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val current = retrofitClient.api.login(LoginRequest(email, password))
                currentUser.value = current
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                onError(e)
            }
        }
    }

    fun logout() {
        currentUser.value = null
    }
}