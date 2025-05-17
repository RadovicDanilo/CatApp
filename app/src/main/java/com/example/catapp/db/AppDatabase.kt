package com.example.catapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.catapp.apitempasas.list.model.BreedEntity
import com.example.catapp.apitempasas.list.model.ImageEntity
import com.example.catapp.apitempasas.repository.BreedDao
import com.example.catapp.apitempasas.repository.ImageDao

@Database(
    entities = [BreedEntity::class, ImageEntity::class],
    version = 1,
)
@TypeConverters(
    JsonTypeConvertor::class,
    ListTypeConverter::class,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun breedDao(): BreedDao

    abstract fun imageDao(): ImageDao
}
