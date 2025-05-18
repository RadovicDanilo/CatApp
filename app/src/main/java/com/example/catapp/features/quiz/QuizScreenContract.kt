package com.example.catapp.features.quiz

import com.example.catapp.data.quiz.model.QuizQuestion

interface QuizScreenContract {
    data class UiState(
        val questions: List<QuizQuestion> = emptyList(),
        val currentQuestionIdx: Int = 0,
        val remainingTime: Int = 300,
        val correctAnswerCount: Int = 0,
        val totalScore: Double = 0.0,
        val hasFinished: Boolean = false,
        val error: Throwable? = null
    )
}
