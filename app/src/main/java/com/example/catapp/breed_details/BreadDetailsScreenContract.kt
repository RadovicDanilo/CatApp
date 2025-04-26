package com.example.catapp.breed_details

import com.example.catapp.users.list.model.DetailedBreadUiModel

interface BreadDetailsScreenContract {

    data class UiState(
        val data: DetailedBreadUiModel? = null,
        val isLoading: Boolean = true,
        val error: Throwable? = null
    )

}