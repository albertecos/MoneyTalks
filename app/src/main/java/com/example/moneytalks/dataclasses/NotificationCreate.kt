package com.example.moneytalks.dataclasses

data class NotificationCreate(
    val userId: String,
    val groupId: String,
    val groupName: String,
    val action: String,
    val amount: Double?,
    val description: String
)
