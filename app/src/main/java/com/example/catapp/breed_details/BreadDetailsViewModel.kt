package com.example.catapp.breed_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapp.breed_details.BreadDetailsScreenContract.UiState
import com.example.catapp.navigation.breadIdOrThrow
import com.example.catapp.apitempasas.list.model.DetailedBreadUiModel
import com.example.catapp.apitempasas.repository.BreadRepository
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
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                val details = DetailedBreadUiModel(repository.fetchBread(breadId))
                val image = repository.getImageById(details.imageId)
                setState { copy(details = details, imageApiModel = image) }

            } catch (error: Exception) {
                setState { copy(error = error) }
            }
        }
    }
}
