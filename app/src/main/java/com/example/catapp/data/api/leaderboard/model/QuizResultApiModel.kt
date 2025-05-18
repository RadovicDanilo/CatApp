package com.example.catapp.data.api.leaderboard.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizResultApiModel(
    val category: Int = 1,
    val nickname: String,
    val result: Double,
    val createdAt: Long
)