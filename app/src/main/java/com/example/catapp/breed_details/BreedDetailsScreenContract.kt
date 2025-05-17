package com.example.catapp.breed_details

import com.example.catapp.apitempasas.list.model.BreedEntity
import com.example.catapp.apitempasas.list.model.ImageEntity
import kotlinx.coroutines.flow.Flow

interface BreedDetailsScreenContract {

    data class UiState(
        val details: Flow<BreedEntity?>? = null,
        val imageApiModel: Flow<ImageEntity?>? = null,
        val error: Throwable? = null
    )
}
