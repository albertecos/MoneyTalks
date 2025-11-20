package com.example.moneytalks.dataclasses

data class SignupResponse(
    val success: Boolean,
    val message: String,
    val user: User?
)