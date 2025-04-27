package com.example.catapp.users.list.model

import com.example.catapp.users.api.model.BreadApiModel

data class DetailedBreadUiModel(
    val id: String,
    val name: String,
    val description: String,
    val imageId: String,
    val countriesOfOrigin: List<String>,
    val temperament: List<String>,
    val lifeSpan: String,
    val weightMetric: String,
    val isIndoor: Boolean,
    val isLapCat: Boolean,
    val isExperimental: Boolean,
    val isHairless: Boolean,
    val isNatural: Boolean,
    val isRare: Boolean,
    val isRex: Boolean,
    val hasSuppressedTail: Boolean,
    val hasShortLegs: Boolean,
    val isHypoallergenic: Boolean,
    val adaptability: Int,
    val affectionLevel: Int,
    val childFriendly: Int,
    val dogFriendly: Int,
    val energyLevel: Int,
    val grooming: Int,
    val healthIssues: Int,
    val intelligence: Int,
    val sheddingLevel: Int,
    val socialNeeds: Int,
    val strangerFriendly: Int,
    val vocalisation: Int,
    val wikipediaUrl: String,
) {
    constructor(apiModel: BreadApiModel) : this(
        id = apiModel.id ?: "",
        name = apiModel.name ?: "",
        description = apiModel.description ?: "",
        imageId = apiModel.referenceImageId ?: "",
        countriesOfOrigin = apiModel.countryCodes?.split(",")?.map { it.trim() } ?: mutableListOf(),
        temperament = apiModel.temperament?.split(",")?.map { it.trim() } ?: mutableListOf(),
        lifeSpan = apiModel.lifeSpan ?: "",
        weightMetric = apiModel.weight?.metric ?: "",
        isIndoor = apiModel.indoor == 1,
        isLapCat = apiModel.lap == 1,
        isExperimental = apiModel.experimental == 1,
        isHairless = apiModel.hairless == 1,
        isNatural = apiModel.natural == 1,
        isRare = apiModel.rare == 1,
        isRex = apiModel.rex == 1,
        hasSuppressedTail = apiModel.suppressedTail == 1,
        hasShortLegs = apiModel.shortLegs == 1,
        isHypoallergenic = apiModel.hypoallergenic == 1,
        adaptability = apiModel.adaptability ?: 0,
        affectionLevel = apiModel.affectionLevel ?: 0,
        childFriendly = apiModel.childFriendly ?: 0,
        dogFriendly = apiModel.dogFriendly ?: 0,
        energyLevel = apiModel.energyLevel ?: 0,
        grooming = apiModel.grooming ?: 0,
        healthIssues = apiModel.healthIssues ?: 0,
        intelligence = apiModel.intelligence ?: 0,
        sheddingLevel = apiModel.sheddingLevel ?: 0,
        socialNeeds = apiModel.socialNeeds ?: 0,
        strangerFriendly = apiModel.strangerFriendly ?: 0,
        vocalisation = apiModel.vocalisation ?: 0,
        wikipediaUrl = apiModel.wikipediaUrl ?: ""
    )

    override fun equals(other: Any?): Boolean {
        return other is DetailedBreadUiModel && id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
