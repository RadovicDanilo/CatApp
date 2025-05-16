package com.example.catapp.apitempasas.repository

import com.example.catapp.apitempasas.api.CatApi
import com.example.catapp.apitempasas.api.model.BreadApiModel
import com.example.catapp.apitempasas.api.model.ImageApiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BreadRepository @Inject constructor(
    private val catApi: CatApi
) {
    suspend fun fetchAllBreads(): List<BreadApiModel> {
        return withContext(Dispatchers.IO) {
            catApi.getAllBreads()
        }
    }

    suspend fun fetchBread(id: String): BreadApiModel {
        return withContext(Dispatchers.IO) {
            catApi.getBread(id)
        }
    }

    suspend fun searchPicturesById(breadId: String): List<ImageApiModel> {
        return withContext(Dispatchers.IO) {
            catApi.searchImagesByBreed(breadId)
        }
    }

    suspend fun getImageById(imageId: String): ImageApiModel {
        return withContext(Dispatchers.IO) {
            catApi.getImageById(imageId)
        }
    }
}
