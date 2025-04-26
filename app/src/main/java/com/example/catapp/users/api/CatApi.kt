package com.example.catapp.users.api

import com.example.catapp.users.api.model.BreadApiModel
import retrofit2.http.GET
import retrofit2.http.Path

interface CatApi {

    @GET("/v1/breeds")
    suspend fun getAllBreads(): List<BreadApiModel>

    @GET("/v1/breeds/{id}")
    suspend fun getBread(
        @Path("id") breadId: String,
    ): BreadApiModel

}
