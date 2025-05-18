package com.example.catapp.data.api

import com.example.catapp.data.api.model.BreedApiModel
import com.example.catapp.data.api.model.ImageApiModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatApi {

    @GET("/v1/breeds")
    suspend fun getAllBreeds(): List<BreedApiModel>

    @GET("/v1/breeds/{id}")
    suspend fun getBreed(
        @Path("id") breedId: String,
    ): BreedApiModel

    @GET("/v1/images/{image_id}")
    suspend fun getImageById(
        @Path("image_id") imageId: String
    ): ImageApiModel

    @GET("/v1/images/search")
    suspend fun searchImagesByBreed(
        @Query("breed_ids") breedId: String,
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 0,
        @Query("order") order: String = "DESC",
    ): List<ImageApiModel>

}
