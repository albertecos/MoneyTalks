package com.example.moneytalks.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytalks.apisetup.RetrofitClient
import com.example.moneytalks.dataclasses.Group
import kotlinx.coroutines.launch

class GroupsViewModel(private val retrofitClient: RetrofitClient = RetrofitClient): ViewModel() {
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

    fun createGroup(
        userId: String,
        groupName: String,
        memberIds: List<String>
    ){
        viewModelScope.launch {
            try {
                val groupCreate = com.example.moneytalks.dataclasses.GroupCreate(
                    name = groupName,
                    members = memberIds
                )
                retrofitClient.api.createGroup(userId, groupCreate)

                fetchGroups(userId)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun editGroup(
        userId: String,
        groupId: String,
        newName: String,
        newMemberIds: List<String>
    ){
        viewModelScope.launch {
            try {
                val groupEdit = com.example.moneytalks.dataclasses.GroupEdit(
                    id = groupId,
                    name = newName,
                    members = newMemberIds
                )
                retrofitClient.api.editGroup(userId, groupEdit)

                val index = groups.indexOfFirst { it.id == groupId }
                if (index != -1) {
                    val updatedGroup = groups[index].copy(name = newName)
                    groups[index] = updatedGroup
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun leaveGroup(
        userId: String,
        groupId: String
    ){
        viewModelScope.launch {
            try {
                retrofitClient.api.leaveGroup(userId, groupId)

                groups.removeAll { it.id == groupId}
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}