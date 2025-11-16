package com.example.moneytalks.DataClasses

import com.google.gson.annotations.SerializedName

data class CreateExpenseRequest(
    @SerializedName("memberId") val memberId: String,
    val amount: Double,
    val description: String,
    val action: String = "expense"
)
