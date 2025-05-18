package com.example.catapp.features.leaderboard

import com.example.catapp.data.api.leaderboard.model.QuizResultApiModel

interface LeaderboardScreenContract {

    data class UiState(
        val results: List<QuizResultApiModel> = emptyList(),
        val error: Throwable? = null,
    )

}
