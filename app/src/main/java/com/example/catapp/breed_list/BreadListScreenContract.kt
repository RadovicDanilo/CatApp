package com.example.catapp.breed_list

import com.example.catapp.users.list.model.SimpleBreadUiModel

interface BreadListScreenContract {

    data class UiState(
        val breads: List<SimpleBreadUiModel> = emptyList(),
        val isLoading: Boolean = true,
        val error: Throwable? = null
    )

    sealed class UiEvent {
        data object RefreshData : UiEvent()
    }

    sealed class SideEffect {
    }

}
