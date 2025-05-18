package com.example.catapp.data.api.leaderboard

import com.example.catapp.data.api.leaderboard.model.LeaderboardPostRequest
import com.example.catapp.data.api.leaderboard.model.LeaderboardPostResponse
import com.example.catapp.data.api.leaderboard.model.QuizResultApiModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface LeaderBoardApi {

    @GET("/leaderboard")
    suspend fun getResults(
        @Query("category") category: Int = 1
    ): List<QuizResultApiModel>

    @POST("/leaderboard")
    suspend fun postResults(
        @Body request: LeaderboardPostRequest
    ): LeaderboardPostResponse
}
