package com.example.testCompose.presentation.injection

import com.example.testCompose.data.db.cache.MoviesCache
import com.example.testCompose.data.db.impl.MoviesRepositoryImpl
import com.example.testCompose.data.db.remote.remote.MoviesRemote
import com.example.testCompose.data.repository.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoviesRepository(moviesRemote: MoviesRemote, moviesCache: MoviesCache): MoviesRepository =
        MoviesRepositoryImpl(moviesRemote, moviesCache)
}