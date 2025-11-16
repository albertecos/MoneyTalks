package com.example.moneytalks.APISetup

import com.example.moneytalks.DataClasses.Balance
import com.example.moneytalks.DataClasses.CreateExpenseRequest
import com.example.moneytalks.DataClasses.Group
import com.example.moneytalks.DataClasses.GroupMember
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MoneyTalksInterface {
    @GET("groups")
    suspend fun getGroups(@Query("userId") memberId: String): List<Group>

    @GET("groupMembers")
    suspend fun getGroupMembers(@Query("groupId") groupId: String): List<GroupMember>

    @POST("createExpense")
    suspend fun createExpense(@Body request: CreateExpenseRequest): retrofit2.Response<Unit>

     @GET("getBalance")
    suspend fun getBalance(
        @Query("groupId") groupId: String,
        @Query("userId") userId: String)
    : Balance

}