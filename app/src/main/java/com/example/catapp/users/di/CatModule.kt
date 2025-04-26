package com.example.catapp.users.di

import com.example.catapp.users.api.CatApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CatModule {

    @Provides
    @Singleton
    fun provideCatApi(retrofit: Retrofit) = retrofit.create<CatApi>()
}