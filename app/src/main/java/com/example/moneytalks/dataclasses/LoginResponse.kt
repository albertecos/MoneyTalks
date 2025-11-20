package com.example.moneytalks.dataclasses

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val user: User?
)