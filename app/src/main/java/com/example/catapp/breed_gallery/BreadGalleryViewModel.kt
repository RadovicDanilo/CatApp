package com.example.catapp.breed_gallery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapp.apitempasas.api.model.ImageApiModel
import com.example.catapp.apitempasas.repository.BreadRepository
import com.example.catapp.breed_gallery.BreadGalleryScreenContract.UiState
import com.example.catapp.navigation.breadIdOrThrow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreadGalleryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, private val repository: BreadRepository
) : ViewModel() {

    val breadId = savedStateHandle.breadIdOrThrow

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
                val images = mutableListOf<ImageApiModel>()
                for (page in 0 until pageCount) {
                    val resp = repository.searchPicturesById(breadId, page = page)
                    images.addAll(resp)
                }
                setState { copy(images = images) }

            } catch (error: Exception) {
                setState { copy(error = error) }
            }
        }
    }
}
