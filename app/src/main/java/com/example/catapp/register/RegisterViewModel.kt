package com.example.catapp.register

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.catapp.account.UserAccount
import com.example.catapp.account.UserAccountStore
import com.example.catapp.register.RegisterScreenContract.UiEvent
import com.example.catapp.register.RegisterScreenContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userAccountStore: UserAccountStore,
) : ViewModel() {

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

    suspend fun submit() {
        Log.d("USER_ACC", "TRYING TO CREATE")

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

        val userAccount =
            UserAccount(current.firstName, current.lastName, current.nickname, current.email)
        userAccountStore.replaceUserAccount(userAccount)

        Log.d("USER_ACC", "CREATED")
        Log.d("USER_ACC", userAccountStore.userAccount.toString())

    }

    private fun isNicknameValid(nickname: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9_]+$")
        return regex.matches(nickname)
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
