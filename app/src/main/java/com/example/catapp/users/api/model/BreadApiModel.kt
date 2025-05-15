package com.example.catapp.users.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BreadApiModel(
    val weight: Weight? = null,
    val id: String? = null,
    val name: String? = null,
    @SerialName("cfa_url") val cfaUrl: String? = null,
    @SerialName("vetstreet_url") val vetstreetUrl: String? = null,
    @SerialName("vcahospitals_url") val vcahospitalsUrl: String? = null,
    val temperament: String? = null,
    val origin: String? = null,
    @SerialName("country_codes") val countryCodes: String? = null,
    @SerialName("country_code") val countryCode: String? = null,
    val description: String? = null,
    @SerialName("life_span") val lifeSpan: String? = null,
    val indoor: Int? = null,
    val lap: Int? = null,
    @SerialName("alt_names") val altNames: String? = null,
    val adaptability: Int? = null,
    @SerialName("affection_level") val affectionLevel: Int? = null,
    @SerialName("child_friendly") val childFriendly: Int? = null,
    @SerialName("dog_friendly") val dogFriendly: Int? = null,
    @SerialName("energy_level") val energyLevel: Int? = null,
    val grooming: Int? = null,
    @SerialName("health_issues") val healthIssues: Int? = null,
    val intelligence: Int? = null,
    @SerialName("shedding_level") val sheddingLevel: Int? = null,
    @SerialName("social_needs") val socialNeeds: Int? = null,
    @SerialName("stranger_friendly") val strangerFriendly: Int? = null,
    val vocalisation: Int? = null,
    val experimental: Int? = null,
    val hairless: Int? = null,
    val natural: Int? = null,
    val rare: Int? = null,
    val rex: Int? = null,
    @SerialName("suppressed_tail") val suppressedTail: Int? = null,
    @SerialName("short_legs") val shortLegs: Int? = null,
    @SerialName("wikipedia_url") val wikipediaUrl: String? = null,
    val hypoallergenic: Int? = null,
    @SerialName("reference_image_id") val referenceImageId: String? = null,
)

@Serializable
data class Weight(
    val imperial: String? = null,
    val metric: String? = null,
)
