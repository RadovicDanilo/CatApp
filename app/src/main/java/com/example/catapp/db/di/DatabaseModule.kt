package com.example.catapp.db.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.catapp.db.AppDatabase
import com.example.catapp.db.AppDatabaseBuilder
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(builder: AppDatabaseBuilder): AppDatabase {
        return builder.build()
    }

}
