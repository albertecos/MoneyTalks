package com.example.moneytalks.DataClasses

import java.util.Date

data class Notification(
    val id: String,
    val action: String,
    val groupId: String,
    val groupName: String,
    val userId: String,
    val amount: Double?,
    val date: String,
    val seen: Boolean,
    val description: String
)
