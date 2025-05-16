package com.example.catapp.breed_gallery

import com.example.catapp.apitempasas.api.model.ImageApiModel

interface BreadGalleryScreenContract {

    data class UiState(
        val images: List<ImageApiModel> = emptyList<ImageApiModel>(), val error: Throwable? = null
    )

}