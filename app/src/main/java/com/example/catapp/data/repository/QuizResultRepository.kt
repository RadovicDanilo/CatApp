package com.example.catapp.data.repository

import com.example.catapp.data.account_store.UserAccountStore
import com.example.catapp.data.api.leaderboard.LeaderBoardApi
import com.example.catapp.data.api.leaderboard.model.LeaderboardPostRequest
import com.example.catapp.data.api.leaderboard.model.QuizResultApiModel
import com.example.catapp.data.db.AppDatabase
import com.example.catapp.data.db.model.QuizResultEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuizResultRepository @Inject constructor(
    private val leaderBoardApi: LeaderBoardApi,
    private val userAccountStore: UserAccountStore,
    database: AppDatabase,
) {
    private val quizDao = database.quizDao()

    suspend fun fetchLeaderboard(): List<QuizResultApiModel> {
        return leaderBoardApi.getResults()
    }

    suspend fun postResult(leaderboardPostRequest: LeaderboardPostRequest) {
        val res = leaderBoardApi.postResults(leaderboardPostRequest)
        val currentUser = userAccountStore.userAccount.value
        if (currentUser == null) return

        val updatedUser = currentUser.copy(
            bestRank = res.ranking.coerceAtMost(currentUser.bestRank ?: Int.MAX_VALUE)
        )
        userAccountStore.replaceUserAccount(updatedUser)
    }

    suspend fun addLocalUsersResults(quizResultEntity: QuizResultEntity) {
        withContext(Dispatchers.IO) {
            quizDao.upsert(quizResultEntity)
        }
    }

    fun observeLocalUsersResults(nickname: String): Flow<List<QuizResultEntity>> {
        return quizDao.observeByNickname(nickname)
    }
}
