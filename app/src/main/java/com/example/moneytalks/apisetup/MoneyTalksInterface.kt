package com.example.moneytalks.apisetup

import com.example.moneytalks.dataclasses.Balance
import com.example.moneytalks.dataclasses.Expense
import com.example.moneytalks.dataclasses.Notification
import com.example.moneytalks.dataclasses.Group
import com.example.moneytalks.dataclasses.GroupCreate
import com.example.moneytalks.dataclasses.GroupEdit
import com.example.moneytalks.dataclasses.LoginRequest
import com.example.moneytalks.dataclasses.NotificationCreate
import com.example.moneytalks.dataclasses.User
import com.example.moneytalks.dataclasses.UserCreate
import com.example.moneytalks.dataclasses.PayOwedData
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

    @POST("createExpense")
    suspend fun createExpense(
        @Query("userId") userId: String,
        @Body expense: Expense
    )

    @POST("editGroup")
    suspend fun editGroup(
        @Query("userId") userId: String,
        @Body group: GroupEdit)

    @POST("leaveGroup")
    suspend fun leaveGroup(
        @Query("userId") userId: String,
        @Query("groupId") groupId: String): retrofit2.Response<Unit>

    @GET("getNotifications")
    suspend fun getNotifications(@Query("userId") userId: String): List<Notification>

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): User

    @POST("createNotification")
    suspend fun createNotification(
        @Query("userId") userId: String,
        @Body notification: NotificationCreate)

    @POST("signup")
    suspend fun signup(
        @Body user: UserCreate
    ) : User

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

    @POST("payOwed")
    suspend fun payOwed(
        @Query("userId") userId: String,
        @Body data: PayOwedData
    )

    @GET("searchUsers")
    suspend fun searchUsers(@Query("username") query: String): List<User>

    @POST("updateUser")
    suspend fun updateUser(@Body user: User): User

    @GET("expenseHistory")
    suspend fun getExpenseHistory(
        @Query("groupId") groupId: String
    ): List<Expense>

    @POST("sendReminder")
    suspend fun sendReminder(
        @Query("userId") userId: String,
        @Query("groupId") groupId: String
    )
}