package com.example.catapp.data.api.cat.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageApiModel(
    val id: String? = null,
    val url: String? = null,
)