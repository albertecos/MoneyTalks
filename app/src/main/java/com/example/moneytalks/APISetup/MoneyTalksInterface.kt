package com.example.moneytalks.APISetup

import com.example.moneytalks.DataClasses.Notification
import com.example.moneytalks.DataClasses.Group
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MoneyTalksInterface {
    @GET("groups")
    suspend fun getGroups(@Query("userId") userId: String): List<Group>

    @GET("getNotifications")
    suspend fun getNotifications(@Query("userId") userId: String): List<Notification>

    @POST("acceptInvite")
    suspend fun acceptInvite(@Query("memberId") memberId: String)

    @POST("declineInvite")
    suspend fun declineInvite(@Query("memberId") memberId: String)
}