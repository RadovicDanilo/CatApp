package com.example.catapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.catapp.data.db.model.BreedEntity
import com.example.catapp.data.db.model.ImageEntity
import com.example.catapp.data.db.dao.BreedDao
import com.example.catapp.data.db.dao.ImageDao

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
