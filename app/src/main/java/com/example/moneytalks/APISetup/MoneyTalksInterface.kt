package com.example.moneytalks.APISetup

import com.example.moneytalks.DataClasses.Balance
import com.example.moneytalks.DataClasses.Notification
import com.example.moneytalks.DataClasses.Group
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MoneyTalksInterface {
    @GET("groups")
    suspend fun getGroups(@Query("userId") userId: String): List<Group>

    @POST("leaveGroup")
    suspend fun leaveGroup(
        @Query("userId") userId: String,
        @Query("groupId") groupId: String): retrofit2.Response<Unit>

    @GET("getNotifications")
    suspend fun getNotifications(@Query("userId") userId: String): List<Notification>

    @POST("acceptInvite")
    suspend fun acceptInvite(
        @Query("userId") userId: String,
        @Query("groupId") groupId: String)

    @POST("declineInvite")
    suspend fun declineInvite(@Query("userId") userId: String)

    @GET("getBalance")
    suspend fun getBalance(
        @Query("groupId") groupId: String,
        @Query("userId") userId: String)
    : Balance
}