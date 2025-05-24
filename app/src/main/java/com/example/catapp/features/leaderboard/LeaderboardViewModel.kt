package com.example.catapp.features.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapp.data.repository.QuizResultRepository
import com.example.catapp.features.leaderboard.LeaderboardScreenContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val quizResultResultRepository: QuizResultRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: UiState.() -> UiState) = _state.update(reducer)

    init {
        viewModelScope.launch {
            setState { copy(loading = true) }
            val results = quizResultResultRepository.fetchLeaderboard()
            setState {
                copy(
                    results = results, loading = false
                )
            }
        }
    }
}
