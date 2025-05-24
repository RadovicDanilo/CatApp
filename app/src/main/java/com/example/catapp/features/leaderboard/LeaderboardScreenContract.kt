package com.example.catapp.features.leaderboard

import com.example.catapp.data.api.leaderboard.model.QuizResultApiModel

interface LeaderboardScreenContract {

    data class UiState(
        val results: List<QuizResultApiModel> = emptyList(),
        val loading: Boolean = true,
        val error: Throwable? = null,
    )

}
