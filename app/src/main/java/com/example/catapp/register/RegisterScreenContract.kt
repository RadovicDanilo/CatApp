package com.example.catapp.register

interface RegisterScreenContract {

    data class UiState(
        val firstName: String = "",
        val lastName: String = "",
        val nickname: String = "",
        val email: String = "",
        val error: Throwable? = null,
    )

    sealed class UiEvent {
        data class UpdateFirstName(val firstName: String) : UiEvent()
        data class UpdateLastName(val lastName: String) : UiEvent()
        data class UpdateNickname(val nickname: String) : UiEvent()
        data class UpdateEmail(val email: String) : UiEvent()
    }
}
