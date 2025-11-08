package com.example.moneytalks.DataClasses


data class Group(
    val id: String,
    val name: String,
    val description: String,
    val members: List<MemberRef>
)
