package com.example.catapp.features.breed_details

import com.example.catapp.data.db.model.BreedEntity
import com.example.catapp.data.db.model.ImageEntity
import kotlinx.coroutines.flow.Flow

interface BreedDetailsScreenContract {

    data class UiState(
        val details: Flow<BreedEntity?>? = null,
        val imageApiModel: Flow<ImageEntity?>? = null,
        val error: Throwable? = null
    )
}
