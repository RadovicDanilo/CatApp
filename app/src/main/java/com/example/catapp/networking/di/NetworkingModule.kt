package com.example.catapp.networking.di

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

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().addInterceptor {
        val apiKey =  ""

        val updatedRequest = it.request().newBuilder().addHeader("x-api-key", apiKey).build()
        it.proceed(updatedRequest)
    }.addInterceptor(
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }).build()

    @Singleton
    @Provides
    fun provideRetrofitClient(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder().baseUrl("https://api.thecatapi.com").client(okHttpClient)
            .addConverterFactory(NetworkingJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}
