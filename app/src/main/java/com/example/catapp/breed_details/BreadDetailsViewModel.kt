package com.example.catapp.breed_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapp.breed_details.BreadDetailsScreenContract.UiState
import com.example.catapp.navigation.breadIdOrThrow
import com.example.catapp.users.list.model.DetailedBreadUiModel
import com.example.catapp.users.repository.BreadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreadDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, private val repository: BreadRepository
) : ViewModel() {

    private val breadId = savedStateHandle.breadIdOrThrow

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: UiState.() -> UiState) = _state.update(reducer)

    init {
        fetchDetails()
    }

    private fun fetchDetails() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            try {
                val data = DetailedBreadUiModel(repository.fetchBread(breadId))
                setState { copy(data = data) }
            } catch (error: Exception) {
                setState { copy(error = error) }
            } finally {
                setState { copy(isLoading = false) }
            }
        }
    }
}
