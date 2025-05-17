package com.example.catapp.breed_gallery

import com.example.catapp.apitempasas.list.model.ImageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface BreedGalleryScreenContract {

    data class UiState(
        val images: Flow<List<ImageEntity>> = flow { emit(emptyList<ImageEntity>()) },
        val error: Throwable? = null,
    )

}