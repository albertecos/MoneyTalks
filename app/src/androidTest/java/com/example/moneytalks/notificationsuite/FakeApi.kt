package com.example.moneytalks.notificationsuite

import com.example.moneytalks.apisetup.MoneyTalksInterface
import com.example.moneytalks.dataclasses.Balance
import com.example.moneytalks.dataclasses.Expense
import com.example.moneytalks.dataclasses.Group
import com.example.moneytalks.dataclasses.GroupCreate
import com.example.moneytalks.dataclasses.GroupEdit
import com.example.moneytalks.dataclasses.LoginRequest
import com.example.moneytalks.dataclasses.Notification
import com.example.moneytalks.dataclasses.NotificationCreate
import com.example.moneytalks.dataclasses.User
import com.example.moneytalks.dataclasses.UserCreate
import retrofit2.Response

class FakeApi(private val behavior: () -> Unit) : MoneyTalksInterface {

    override suspend fun createExpense(
        userId: String,
        expense: Expense
    ) {
        behavior()
    }

    override suspend fun getGroups(userId: String): List<Group> {
        TODO("Not yet implemented")
    }

    override suspend fun createGroup(
        userId: String,
        group: GroupCreate
    ) {
        TODO("Not yet implemented")
    }



    override suspend fun editGroup(userId: String, group: GroupEdit) {
        TODO("Not yet implemented")
    }

    override suspend fun leaveGroup(
        userId: String,
        groupId: String
    ): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getNotifications(userId: String): List<Notification> {
        TODO("Not yet implemented")
    }

    override suspend fun login(request: LoginRequest): User {
        TODO("Not yet implemented")
    }

    override suspend fun createNotification(
        userId: String,
        notification: NotificationCreate
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun signup(user: UserCreate): User {
        TODO("Not yet implemented")
    }

    override suspend fun acceptInvite(userId: String, groupId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun declineInvite(notificationId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getBalance(
        groupId: String,
        userId: String
    ): Balance {
        TODO("Not yet implemented")
    }

    override suspend fun searchUsers(query: String): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getExpenseHistory(groupId: String): List<Expense> {
        TODO("Not yet implemented")
    }

    override suspend fun sendReminder(userId: String, groupId: String) {
        TODO("Not yet implemented")
    }
}