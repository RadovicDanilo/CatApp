package com.example.catapp.apitempasas.repository

import androidx.room.withTransaction
import com.example.catapp.apitempasas.api.CatApi
import com.example.catapp.apitempasas.list.model.BreedEntity
import com.example.catapp.apitempasas.list.model.ImageEntity
import com.example.catapp.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BreedRepository @Inject constructor(
    private val catApi: CatApi,
    private val database: AppDatabase,
) {
    private val breedDao = database.breedDao()
    private val imageDao = database.imageDao()

    suspend fun fetchAllBreeds() {
        val apiBreeds = catApi.getAllBreeds()
        val breedEntities = apiBreeds.map { BreedEntity.fromApi(it) }

        database.withTransaction {
            breedDao.upsertAll(breedEntities)
        }
    }

    fun observeAllBreeds(): Flow<List<BreedEntity>> {
        return breedDao.observeAll()
    }

    suspend fun fetchBreed(id: String) {
        val apiBreed = catApi.getBreed(id)
        val entity = BreedEntity.fromApi(apiBreed)

        withContext(Dispatchers.IO) {
            breedDao.upsert(entity)
        }
    }

    fun observeBreed(id: String): Flow<BreedEntity?> {
        return breedDao.observeById(id)
    }

    suspend fun fetchImagesForBreed(
        breedId: String, limit: Int = 10, page: Int = 0, order: String = "DESC"
    ) {
        val images = withContext(Dispatchers.IO) {
            catApi.searchImagesByBreed(breedId, limit, page, order)
        }
        val entities = images.map { ImageEntity(it.id.toString(), it.url.toString(), breedId) }

        database.withTransaction {
            imageDao.upsertAll(entities)
        }
    }

    fun observeImagesForBreed(breedId: String): Flow<List<ImageEntity>> =
        imageDao.observeByBreedId(breedId)

    suspend fun fetchImageById(imageId: String, breedId: String?) {
        val image = withContext(Dispatchers.IO) {
            catApi.getImageById(imageId)
        }
        image.let {
            val entity = ImageEntity(
                it.id.toString(), it.url.toString(), breedId = breedId ?: ""
            )
            imageDao.upsert(entity)
        }
    }

    fun observeImageById(imageId: String): Flow<ImageEntity?> = imageDao.observeById(imageId)
}
