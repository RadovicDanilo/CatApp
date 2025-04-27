package com.example.catapp.breed_details

import com.example.catapp.users.api.model.ImageApiModel
import com.example.catapp.users.list.model.DetailedBreadUiModel

interface BreadDetailsScreenContract {

    data class UiState(
        val details: DetailedBreadUiModel? = null,
        val imageApiModel: ImageApiModel? = null,
        val error: Throwable? = null
    )

}