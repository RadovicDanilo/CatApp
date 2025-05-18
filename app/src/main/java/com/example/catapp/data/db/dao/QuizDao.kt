package com.example.catapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catapp.data.db.model.QuizResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(quizResult: QuizResultEntity)

    @Query("SELECT * FROM quiz_results WHERE nickname = :nickname")
    fun observeByNickname(nickname: String): Flow<List<QuizResultEntity>>
}
