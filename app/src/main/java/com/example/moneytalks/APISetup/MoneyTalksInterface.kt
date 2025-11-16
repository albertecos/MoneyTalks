package com.example.moneytalks.APISetup

import com.example.moneytalks.DataClasses.CreateExpenseRequest
import com.example.moneytalks.DataClasses.Group
import com.example.moneytalks.DataClasses.Member
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MoneyTalksInterface {
    @GET("groups")
    suspend fun getGroups(@Query("memberId") memberId: String): List<Group>

    @GET("groupMembers")
    suspend fun getGroupMembers(@Query("groupId") groupId: String): List<Member>

    @POST("createExpense")
    suspend fun createExpense(@Body request: CreateExpenseRequest): retrofit2.Response<Unit>
}