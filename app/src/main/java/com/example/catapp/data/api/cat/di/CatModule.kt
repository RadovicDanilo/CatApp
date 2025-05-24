package com.example.catapp.data.api.cat.di

import com.example.catapp.data.api.cat.CatApi
import com.example.catapp.networking.di.CatApiQualifier
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
    fun provideCatApi(@CatApiQualifier retrofit: Retrofit): CatApi =
        retrofit.create()
}
