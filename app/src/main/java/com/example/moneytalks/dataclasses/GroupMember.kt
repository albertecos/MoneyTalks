package com.example.moneytalks.dataclasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupMember(
    val id: String,
    val username: String,
    val full_name: String,
    val email: String,
    val password: String,
    val profile_picture: String,
    val accepted: Boolean
) : Parcelable
