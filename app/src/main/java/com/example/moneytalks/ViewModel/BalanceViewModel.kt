package com.example.moneytalks.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytalks.APISetup.RetrofitClient
import com.example.moneytalks.DataClasses.Balance
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