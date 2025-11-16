package com.example.moneytalks.DataClasses

data class GroupMember(
    val id: String,
    val username: String,
    val full_name: String,
    val email: String,
    val password: String,
    val profile_picture: String,
    val accepted: Boolean
)
