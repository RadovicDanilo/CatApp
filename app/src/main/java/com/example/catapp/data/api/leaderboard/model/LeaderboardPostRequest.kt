package com.example.catapp.data.api.leaderboard.model

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardPostRequest(
    val nickname: String,
    val result: Float,
    val category: Int,
)