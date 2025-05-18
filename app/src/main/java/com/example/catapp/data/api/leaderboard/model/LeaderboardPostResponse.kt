package com.example.catapp.data.api.leaderboard.model

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardPostResponse(
    val result: QuizResultApiModel,
    val ranking: Int
)
