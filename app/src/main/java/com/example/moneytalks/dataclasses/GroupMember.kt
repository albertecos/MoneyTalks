package com.example.moneytalks.dataclasses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupMember(
    val id: String, //member ID's
    @SerializedName("user_id")
    val userId: String,
    val username: String,
    val full_name: String,
    val email: String,
    val password: String,
    val profile_picture: String,
    val accepted: Boolean
) : Parcelable
