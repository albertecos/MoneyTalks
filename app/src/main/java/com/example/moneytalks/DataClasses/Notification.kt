package com.example.moneytalks.DataClasses

import java.util.Date

data class ExpenseNotification(
    val id: String,
    val action: String,
    val amount: Double,
    val date: Date
)
