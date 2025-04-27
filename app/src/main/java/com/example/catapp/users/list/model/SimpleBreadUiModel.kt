package com.example.catapp.users.list.model

import com.example.catapp.users.api.model.BreadApiModel

data class SimpleBreadUiModel(
    val id: String,
    val name: String,
    val altNames: String,
    val description: String,
    val traits: Array<String>
) {
    constructor(breadApiModel: BreadApiModel) : this(
        breadApiModel.id ?: "",
        breadApiModel.name ?: "",
        breadApiModel.altNames ?: "",
        breadApiModel.altNames ?: "",
        breadApiModel.temperament?.split(", ")?.toTypedArray() ?: emptyArray()
    )

    override fun equals(other: Any?): Boolean {
        return other is SimpleBreadUiModel && id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
