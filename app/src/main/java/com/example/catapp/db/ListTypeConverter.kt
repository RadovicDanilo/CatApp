package com.example.catapp.db

import androidx.room.TypeConverter

class ListTypeConverter {

    @TypeConverter
    fun fromList(list: List<String>?): String {
        return list?.joinToString(separator = ",") ?: ""
    }

    @TypeConverter
    fun toList(data: String?): List<String> {
        return data?.takeIf { it.isNotBlank() }?.split(",")?.map { it.trim() } ?: emptyList()
    }
}
