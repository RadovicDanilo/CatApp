package com.example.catapp.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CatApiQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LeaderboardApiQualifier
