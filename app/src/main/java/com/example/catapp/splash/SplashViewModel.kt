package com.example.catapp.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapp.account.UserAccountStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userAccountStore: UserAccountStore,
) : ViewModel() {

    private val _hasUserAccount = MutableStateFlow(false)
    val hasUserAccount = _hasUserAccount

    private val _bootCompleted = MutableStateFlow(false)
    val bootCompleted = _bootCompleted

    init {
        viewModelScope.launch {
            _hasUserAccount.emit(userAccountStore.userAccount.value != null)
            _bootCompleted.emit(true)
        }
    }
}
