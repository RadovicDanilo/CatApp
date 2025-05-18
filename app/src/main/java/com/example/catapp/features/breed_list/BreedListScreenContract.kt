package com.example.catapp.features.breed_list

import com.example.catapp.data.db.model.BreedEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface BreedListScreenContract {

    data class UiState(
        val searchTerm: String = "",
        val breeds: Flow<List<BreedEntity>> = flow { emit(emptyList()) },
        val isLoading: Boolean = true,
        val error: Throwable? = null
    )

    sealed class UiEvent {
        data class UpdateSearchTerm(val newTerm: String) : UiEvent()
    }

}
