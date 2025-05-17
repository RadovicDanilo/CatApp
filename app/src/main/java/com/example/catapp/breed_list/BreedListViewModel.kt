package com.example.catapp.breed_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapp.apitempasas.repository.BreedRepository
import com.example.catapp.breed_list.BreedListScreenContract.UiEvent
import com.example.catapp.breed_list.BreedListScreenContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedListViewModel @Inject constructor(
    private val repository: BreedRepository
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
                repository.fetchAllBreeds()
                val breedsList = repository.observeAllBreeds()
                setState { copy(breeds = breedsList) }
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