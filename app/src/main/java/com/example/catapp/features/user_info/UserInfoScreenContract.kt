package com.example.catapp.features.user_info

import com.example.catapp.data.account_store.UserAccount
import com.example.catapp.data.db.model.QuizResultEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UserInfoScreenContract {

    data class UiState(
        val userInfo: StateFlow<UserAccount?>,
        val results: Flow<List<QuizResultEntity>>,
        val error: Throwable? = null,
    )

}
