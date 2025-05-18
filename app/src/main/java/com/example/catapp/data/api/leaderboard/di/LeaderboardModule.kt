package com.example.catapp.data.api.leaderboard.di

import com.example.catapp.data.api.leaderboard.LeaderBoardApi
import com.example.catapp.di.LeaderboardApiQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LeaderboardModule {

    @Provides
    @Singleton
    fun provideLeaderboardApi(@LeaderboardApiQualifier retrofit: Retrofit): LeaderBoardApi =
        retrofit.create()
}