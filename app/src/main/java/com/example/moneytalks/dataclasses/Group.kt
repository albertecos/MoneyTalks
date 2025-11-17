package com.example.moneytalks.dataclasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
    val id: String,
    val name: String,
    val members: List<GroupMember>
) : Parcelable
