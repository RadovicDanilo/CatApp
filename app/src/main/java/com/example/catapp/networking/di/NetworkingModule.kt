// com.example.catapp.networking.di.NetworkingModule.kt
package com.example.catapp.networking.di

import com.example.catapp.di.CatApiQualifier
import com.example.catapp.di.LeaderboardApiQualifier
import com.example.catapp.networking.serialization.NetworkingJson
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Provides
    @Singleton
    fun provideDefaultOkHttpClient(): OkHttpClient = OkHttpClient.Builder().addInterceptor {
            val apiKey = ""
            val updatedRequest = it.request().newBuilder().addHeader("x-api-key", apiKey).build()
            it.proceed(updatedRequest)
        }.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

    @Provides
    @Singleton
    @CatApiQualifier
    fun provideCatRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl("https://api.thecatapi.com/").client(okHttpClient)
            .addConverterFactory(NetworkingJson.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    @LeaderboardApiQualifier
    fun provideLeaderboardRetrofit(): Retrofit =
        Retrofit.Builder().baseUrl("https://rma.finlab.rs/")
            .addConverterFactory(NetworkingJson.asConverterFactory("application/json".toMediaType()))
            .build()
}
