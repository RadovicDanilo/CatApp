package com.example.catapp.account

import kotlinx.serialization.Serializable

@Serializable
data class UserAccount(
    val firstName: String,
    val lastName: String,
    val nickname: String,
    val email: String,
)
