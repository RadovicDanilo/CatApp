package com.example.catapp.apitempasas.api

import com.example.catapp.apitempasas.api.model.BreadApiModel
import com.example.catapp.apitempasas.api.model.ImageApiModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatApi {

    @GET("/v1/breeds")
    suspend fun getAllBreads(): List<BreadApiModel>

    @GET("/v1/breeds/{id}")
    suspend fun getBread(
        @Path("id") breadId: String,
    ): BreadApiModel

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
