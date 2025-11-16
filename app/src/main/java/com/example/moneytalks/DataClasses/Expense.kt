package com.example.moneytalks.DataClasses

import com.google.gson.annotations.SerializedName

data class Expense(
    val id: String,
    @SerializedName("memberId") val memberId: String,
    val date: String,
    val amount: Double,
    val description: String,
    val action: String
)
