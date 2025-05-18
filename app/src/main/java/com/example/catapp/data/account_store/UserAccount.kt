package com.example.catapp.data.account_store

import kotlinx.serialization.Serializable

@Serializable
data class UserAccount(
    val firstName: String,
    val lastName: String,
    val nickname: String,
    val email: String,
    var bestRank: Int?,
)
