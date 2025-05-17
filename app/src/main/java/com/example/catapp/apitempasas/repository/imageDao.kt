package com.example.catapp.apitempasas.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catapp.apitempasas.list.model.ImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Query("SELECT * FROM images WHERE breedId = :breedId")
    fun observeByBreedId(breedId: String): Flow<List<ImageEntity>>

    @Query("SELECT * FROM images WHERE id = :imageId LIMIT 1")
    fun observeById(imageId: String): Flow<ImageEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(image: ImageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(images: List<ImageEntity>)
}
