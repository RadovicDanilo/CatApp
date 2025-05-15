package com.example.catapp.users.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageApiModel(
    val url: String? = null,
)