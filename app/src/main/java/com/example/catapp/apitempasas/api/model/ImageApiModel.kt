package com.example.catapp.apitempasas.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageApiModel(
    val id: String? = null,
    val url: String? = null,
)