package com.example.catapp.apitempasas.list.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey val id: String, val url: String, val breedId: String
)
