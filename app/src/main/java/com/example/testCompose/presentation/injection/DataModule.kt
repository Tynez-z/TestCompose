package com.example.testCompose.presentation.injection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.testCompose.common.LanguageDataStore
import com.example.testCompose.data.db.cache.MoviesCache
import com.example.testCompose.data.db.cache.MoviesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideLanguageDataStore(
        @ApplicationContext context: Context,
        json: Json
    ): LanguageDataStore {
        return LanguageDataStore(json, context.preferencesDataStore)
    }

    @Provides
    @Singleton
    fun provideMoviesDatabase(@ApplicationContext context: Context): MoviesDatabase =
        MoviesDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideMoviesCache(moviesDatabase: MoviesDatabase): MoviesCache = moviesDatabase.moviesDao
}