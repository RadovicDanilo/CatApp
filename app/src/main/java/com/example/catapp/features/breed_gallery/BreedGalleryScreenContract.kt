package com.example.catapp.features.breed_gallery

import com.example.catapp.data.db.model.ImageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface BreedGalleryScreenContract {

    data class UiState(
        val images: Flow<List<ImageEntity>> = flow { emit(emptyList<ImageEntity>()) },
        val error: Throwable? = null,
    )

}