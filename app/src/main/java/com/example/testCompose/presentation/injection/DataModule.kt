package com.example.testCompose.presentation.injection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.testCompose.common.LanguageDataStore
import com.example.testCompose.data.local.datasource.MoviesLocalDataSource
import com.example.testCompose.data.local.datasource.MoviesLocalDataSourceImpl
import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.data.repository.MoviesRepositoryImpl
import dagger.Binds
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
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(impl: MoviesLocalDataSourceImpl): MoviesLocalDataSource

    @Binds
    @Singleton
    abstract fun bindRepository(impl: MoviesRepositoryImpl): MoviesRepository

    companion object {
        //Need @Provides — uses context.preferencesDataStore extension
        @Provides
        @Singleton
        fun provideLanguageDataStore(
            @ApplicationContext context: Context,
            json: Json
        ): LanguageDataStore {
            return LanguageDataStore(json, context.preferencesDataStore)
        }
    }
}