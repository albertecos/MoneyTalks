package com.example.moneytalks.dataclasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Expense (
    val groupMemberId: String,
    val groupId: String,
    val amount: Double,
    val description: String,
    val action: String,
) : Parcelable