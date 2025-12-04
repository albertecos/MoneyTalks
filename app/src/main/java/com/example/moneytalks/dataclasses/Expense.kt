package com.example.moneytalks.dataclasses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Expense (
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("member_id")
    val memberId: String,
    val groupId: String,
    val amount: Double,
    val description: String,
    val action: String,
    val payers: List<String>
) : Parcelable