package com.example.moneytalks.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytalks.APISetup.RetrofitClient
import com.example.moneytalks.DataClasses.Group
import kotlinx.coroutines.launch

class GroupsViewModel(private val retrofitClient: RetrofitClient): ViewModel() {
    var groups = mutableStateListOf<Group>()

    fun fetchGroups(memberId: String) {
        groups.clear()
        viewModelScope.launch{
            try{
                val response = retrofitClient.api.getGroups(memberId)
                groups.addAll(response)
            }catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}