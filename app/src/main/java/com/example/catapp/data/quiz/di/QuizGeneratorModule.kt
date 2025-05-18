package com.example.catapp.data.quiz.di

import com.example.catapp.data.quiz.QuizGenerator
import com.example.catapp.data.repository.BreedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QuizGeneratorModule {

    @Provides
    @Singleton
    fun provideQuizGenerator(
        breedRepository: BreedRepository
    ): QuizGenerator {
        return QuizGenerator(breedRepository)
    }
}
