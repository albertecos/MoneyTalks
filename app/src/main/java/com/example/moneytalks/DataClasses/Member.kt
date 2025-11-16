package com.example.moneytalks.DataClasses

import com.google.gson.annotations.SerializedName

data class Member (
    val id: String,
    @SerializedName("groupId") val groupId: String,
    @SerializedName("userId") val userId: String,
    val accepted: Boolean
)