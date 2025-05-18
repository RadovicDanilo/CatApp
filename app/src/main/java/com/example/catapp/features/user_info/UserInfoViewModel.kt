package com.example.catapp.features.user_info

import androidx.lifecycle.ViewModel
import com.example.catapp.data.account_store.UserAccountStore
import com.example.catapp.data.repository.QuizResultRepository
import com.example.catapp.features.user_info.UserInfoScreenContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    userAccountStore: UserAccountStore, quizResultResultRepository: QuizResultRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        UiState(
            results = quizResultResultRepository.observeLocalUsersResults(userAccountStore.userAccount.value!!.nickname),
            userInfo = userAccountStore.userAccount
        )
    )
    val state = _state.asStateFlow()

}
