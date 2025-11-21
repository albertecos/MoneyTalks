package com.example.moneytalks.apisetup

import com.example.moneytalks.dataclasses.Balance
import com.example.moneytalks.dataclasses.Notification
import com.example.moneytalks.dataclasses.Group
import com.example.moneytalks.dataclasses.GroupCreate
import com.example.moneytalks.dataclasses.GroupEdit
import com.example.moneytalks.dataclasses.User
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Body

interface MoneyTalksInterface {
    @GET("groups")
    suspend fun getGroups(@Query("userId") userId: String): List<Group>

    @POST("createGroup")
    suspend fun createGroup(
        @Query("userId") userId: String,
        @Body group: GroupCreate)

    @POST("editGroup")
    suspend fun editGroup(@Body group: GroupEdit)

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
    suspend fun declineInvite(@Query("notificationId") notificationId: String)

    @GET("getBalance")
    suspend fun getBalance(
        @Query("groupId") groupId: String,
        @Query("userId") userId: String)
    : Balance

    @GET("searchUsers")
    suspend fun searchUsers(@Query("username") query: String): List<User>
    
    @POST("updateUser")
    suspend fun updateUser(@Body user: User): User

    @POST("enableNotifications")
    suspend fun enableNotifications(@Query("userId") userId: String)

    @POST("disableNotifications")
    suspend fun disableNotifications(@Query("userId") userId: String)
}