package com.example.catapp.register

import androidx.lifecycle.ViewModel
import com.example.catapp.register.RegisterScreenContract.UiEvent
import com.example.catapp.register.RegisterScreenContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: UiState.() -> UiState) = _state.update(reducer)

    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.UpdateFirstName -> setState { copy(firstName = event.firstName) }
            is UiEvent.UpdateLastName -> setState { copy(lastName = event.lastName) }
            is UiEvent.UpdateNickname -> setState { copy(nickname = event.nickname) }
            is UiEvent.UpdateEmail -> setState { copy(email = event.email) }
        }
    }

    fun submit() {
        val current = state.value

        setState { copy(error = null) }

        if (current.firstName.isBlank() || current.lastName.isBlank()) {
            setState { copy(error = IllegalArgumentException("First and last name are required.")) }
            return
        }

        if (!isNicknameValid(current.nickname)) {
            setState { copy(error = IllegalArgumentException("Nickname can only contain letters, numbers, and underscores.")) }
            return
        }

        if (!isEmailValid(current.email)) {
            setState { copy(error = IllegalArgumentException("Invalid email address.")) }
            return
        }

        //TODO save to db/ds

    }

    private fun isNicknameValid(nickname: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9_]+$")
        return regex.matches(nickname)
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
