package com.example.catapp.breed_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapp.apitempasas.repository.BreedRepository
import com.example.catapp.breed_details.BreedDetailsScreenContract.UiState
import com.example.catapp.navigation.BreedIdOrThrow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, private val repository: BreedRepository
) : ViewModel() {

    val breedId = savedStateHandle.BreedIdOrThrow

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: UiState.() -> UiState) = _state.update(reducer)

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                repository.fetchBreed(breedId)
                val detailsFlow = repository.observeBreed(breedId)

                val imageId: String = detailsFlow.mapNotNull { it?.imageId }.first()

                repository.fetchImageById(imageId, breedId)
                val imageFlow = repository.observeImageById(imageId)

                setState {
                    copy(
                        details = detailsFlow, imageApiModel = imageFlow
                    )
                }

            } catch (error: Exception) {
                setState { copy(error = error) }
            }
        }
    }

}
