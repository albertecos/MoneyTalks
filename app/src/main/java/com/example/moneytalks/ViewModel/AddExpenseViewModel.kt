package com.example.moneytalks.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytalks.APISetup.RetrofitClient
import com.example.moneytalks.DataClasses.CreateExpenseRequest
import com.example.moneytalks.DataClasses.GroupMember
import kotlinx.coroutines.launch

class AddExpenseViewModel(private val retrofitClient: RetrofitClient = RetrofitClient) :
    ViewModel() {
    var members = mutableStateListOf<GroupMember>()
        private set
    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set
    fun fetchMembers(groupId: String) {
        members.clear()
        viewModelScope.launch {
            try {
                val response = retrofitClient.api.getGroupMembers(groupId)
                members.addAll(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createExpenseForGroup(
        groupId: String,
        memberId: String,
        amount: Double,
        description: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (members.isEmpty()) {
                    fetchMembers(groupId)
                }

                val request = CreateExpenseRequest(
                    memberId = memberId,
                    amount = amount,
                    description = description,
                    action = "expense"
                )

                val response = retrofitClient.api.createExpense(request)
                if (response.isSuccessful) {
                    onSuccess()
                }else{
                    onError("Could not create expense ${response.body()} body: ${response.errorBody()?.string()}.")
                }
            }catch(e: Exception){
                e.printStackTrace()
                onError("Error occurred: ${e.message}")
            }
        }
    }
}