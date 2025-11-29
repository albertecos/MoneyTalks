package com.example.moneytalks.dataclasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Expense (
    val userId: String,
    val groupId: String,
    val amount: Double,
    val description: String,
    val action: String,
    val payers: List<GroupMember>
) : Parcelable