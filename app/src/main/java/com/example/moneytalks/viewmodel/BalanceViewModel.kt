package com.example.moneytalks.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytalks.apisetup.RetrofitClient
import com.example.moneytalks.dataclasses.Balance
import kotlinx.coroutines.launch

class BalanceViewModel(private val retrofitClient: RetrofitClient = RetrofitClient): ViewModel() {
    var balance = mutableStateOf<Balance?>(null)

    private val _membersBalances = mutableStateMapOf<String, Double>()
    val memberBalances: Map<String, Double> get() = _membersBalances

    fun fetchBalance(groupId: String, userId: String) {
        viewModelScope.launch{
            try{
                val response = retrofitClient.api.getBalance(groupId, userId)
                val valueDouble = response.balance.toDouble()

                if(_isCurrentUser(userId)){
                    balance.value = response
                }

                _membersBalances[userId] = valueDouble
            }catch (e: Exception) {
                e.printStackTrace()
                balance.value = null
                _membersBalances[userId] = 0.0
            }

        }
    }

    private fun _isCurrentUser(userId: String): Boolean = false

    fun payOwed(
        groupId: String,
        userId: String,
    ) {
        viewModelScope.launch {
            try {
                retrofitClient.api.payOwed(
                    userId,
                    com.example.moneytalks.dataclasses.PayOwedData(
                        groupId = groupId
                    )
                )
                // After payment, refresh balance
                fetchBalance(groupId, userId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}