package com.example.catapp.breed_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapp.breed_list.BreadListScreenContract.UiEvent
import com.example.catapp.breed_list.BreadListScreenContract.UiState
import com.example.catapp.users.list.model.SimpleBreadUiModel
import com.example.catapp.users.repository.BreadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreadListViewModel @Inject constructor(
    private val repository: BreadRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: UiState.() -> UiState) = _state.update(reducer)

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            try {
                val breadsList = repository.fetchAllBreads()
                    .map { breadApiModel -> SimpleBreadUiModel(breadApiModel) }
                setState { copy(breads = breadsList) }
            } catch (error: Exception) {
                setState { copy(error = error) }
            } finally {

                setState { copy(isLoading = false) }
            }
        }
    }

    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.UpdateSearchTerm -> {
                setState { copy(searchTerm = event.newTerm) }
            }
        }
    }
}