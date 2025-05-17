package com.example.catapp.apitempasas.list.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.catapp.apitempasas.api.model.BreedApiModel

@Entity(tableName = "breeds")
data class BreedEntity(
    @PrimaryKey val id: String,
    val name: String,
    val altNames: String,
    val description: String,
    val imageId: String,
    val countriesOfOrigin: List<String>,
    val temperament: List<String>,
    val lifeSpan: String,
    val isRare: Boolean,
    val adaptability: Int,
    val childFriendly: Int,
    val dogFriendly: Int,
    val energyLevel: Int,
    val healthIssues: Int,
    val intelligence: Int,
    val wikipediaUrl: String
) {
    companion object {
        fun fromApi(api: BreedApiModel): BreedEntity = BreedEntity(
            id = api.id.orEmpty(),
            name = api.name.orEmpty(),
            altNames = api.altNames.orEmpty(),
            description = api.description.orEmpty(),
            imageId = api.referenceImageId.orEmpty(),
            countriesOfOrigin = api.countryCodes?.split(",")?.map { it.trim() } ?: emptyList(),
            temperament = api.temperament?.split(",")?.map { it.trim() } ?: emptyList(),
            lifeSpan = api.lifeSpan.orEmpty(),
            isRare = api.rare == 1,
            adaptability = api.adaptability ?: 0,
            childFriendly = api.childFriendly ?: 0,
            dogFriendly = api.dogFriendly ?: 0,
            energyLevel = api.energyLevel ?: 0,
            healthIssues = api.healthIssues ?: 0,
            intelligence = api.intelligence ?: 0,
            wikipediaUrl = api.wikipediaUrl.orEmpty())
    }
}
