package com.example.catapp.breed_gallery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapp.apitempasas.list.model.ImageEntity
import com.example.catapp.apitempasas.repository.BreedRepository
import com.example.catapp.breed_gallery.BreedGalleryScreenContract.UiState
import com.example.catapp.navigation.BreedIdOrThrow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedGalleryViewModel @Inject constructor(
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
                val pageCount = 3
                for (page in 0 until pageCount) {
                    repository.fetchImagesForBreed(breedId, page)
                }
                var images = repository.observeImagesForBreed(breedId)

                if (images.mapNotNull { it.size }.first() == 0) {
                    images = flow { emit(emptyList<ImageEntity>()) }
                }

                setState { copy(images = images) }

            } catch (error: Exception) {
                setState { copy(error = error) }
            }
        }
    }
}
