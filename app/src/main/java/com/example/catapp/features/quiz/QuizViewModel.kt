package com.example.catapp.features.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapp.data.account_store.UserAccountStore
import com.example.catapp.data.api.leaderboard.model.LeaderboardPostRequest
import com.example.catapp.data.db.model.QuizResultEntity
import com.example.catapp.data.quiz.QuizGenerator
import com.example.catapp.data.repository.QuizResultRepository
import com.example.catapp.features.quiz.QuizScreenContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val userAccountStore: UserAccountStore,
    private val quizResultRepository: QuizResultRepository,
    private val quizGenerator: QuizGenerator
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: UiState.() -> UiState) = _state.update(reducer)

    private val maxTime = 300

    init {
        viewModelScope.launch {
            val questions = quizGenerator.generateQuiz()
            setState { copy(questions = questions) }
            startCountdown()
        }
    }

    private fun startCountdown() {
        viewModelScope.launch {
            for (time in maxTime downTo 0) {
                if (state.value.hasFinished) break
                setState { copy(remainingTime = time) }
                delay(1000L)
            }
            setState {
                copy(questions = questions)
            }
            finish()
        }
    }

    suspend fun setAnswerAndAdvance(selectedIndex: Int) {
        val currentIdx = state.value.currentQuestionIdx
        val questions = state.value.questions.toMutableList()
        questions[currentIdx].answerIndex = selectedIndex

        if (currentIdx < questions.lastIndex) {
            setState {
                copy(questions = questions, currentQuestionIdx = currentIdx + 1)
            }
        } else {
            setState {
                copy(questions = questions)
            }
            finish()
        }
    }

    suspend fun finish() {
        if (state.value.hasFinished) return

        setState {
            copy(hasFinished = true)
        }
        val timeLeft = state.value.remainingTime
        val correctAnswersCount =
            state.value.questions.count { it.correctOptionIndex == it.answerIndex }
        val totalScore =
            if (timeLeft == 0) 0.0 else correctAnswersCount * 2.5 * (1 + (timeLeft + 120).toDouble() / maxTime)

        quizResultRepository.addLocalUsersResults(
            QuizResultEntity(
                nickname = userAccountStore.userAccount.value!!.nickname,
                result = totalScore.coerceAtMost(100.0)
            )
        )
        setState {
            copy(
                correctAnswerCount = correctAnswersCount,
                totalScore = totalScore.coerceAtMost(100.0)
            )
        }
    }

    // TODO: Fix this
    // TODO: improve visuals, use typography and proper colors everywhere
    suspend fun submitResults() {
        val nickname = userAccountStore.userAccount.value?.nickname ?: "Anonymous"

        val leaderboardPostReq = LeaderboardPostRequest(
            nickname = nickname, result = state.value.totalScore.coerceAtMost(100.0)
        )
        quizResultRepository.postResult(leaderboardPostReq)
    }
}
