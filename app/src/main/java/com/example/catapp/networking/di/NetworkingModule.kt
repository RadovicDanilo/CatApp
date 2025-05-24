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

    private const val CAT_API_KEY =
        "live_Fsf6VdPMgKL5YMSlKo9OWrefHuCx6qCA9nLPNnnyb8vFuSvpqLCmH70kn9v62JPR"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    @CatApiQualifier
    fun provideCatOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("x-api-key", CAT_API_KEY)
                .build()
            chain.proceed(request)
        }
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    @LeaderboardApiQualifier
    fun provideLeaderboardOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    @CatApiQualifier
    fun provideCatRetrofit(
        @CatApiQualifier okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.thecatapi.com/")
        .client(okHttpClient)
        .addConverterFactory(NetworkingJson.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    @LeaderboardApiQualifier
    fun provideLeaderboardRetrofit(
        @LeaderboardApiQualifier okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://rma.finlab.rs/")
        .client(okHttpClient)
        .addConverterFactory(NetworkingJson.asConverterFactory("application/json".toMediaType()))
        .build()
}
