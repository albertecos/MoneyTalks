package com.example.moneytalks.dataclasses

data class SignupRequest(
    val username: String,
    val full_name: String,
    val email: String,
    val password: String
)