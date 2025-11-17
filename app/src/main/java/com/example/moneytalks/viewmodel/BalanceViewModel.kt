package com.example.moneytalks.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytalks.apisetup.RetrofitClient
import com.example.moneytalks.dataclasses.Balance
import kotlinx.coroutines.launch

class BalanceViewModel(private val retrofitClient: RetrofitClient = RetrofitClient): ViewModel() {
    var balance = mutableStateOf<Balance?>(null)

    fun fetchBalance(groupId: String, userId: String) {
        viewModelScope.launch{
            try{
                val response = retrofitClient.api.getBalance(groupId, userId)
                balance.value = response
            }catch (e: Exception) {
                e.printStackTrace()
                balance.value = null
            }

        }
    }
}