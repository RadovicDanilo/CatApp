package com.example.catapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catapp.data.db.model.BreedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(breed: BreedEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(breeds: List<BreedEntity>)

    @Query("SELECT * FROM breeds")
    fun observeAll(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM breeds WHERE id = :id")
    fun observeById(id: String): Flow<BreedEntity?>
}
