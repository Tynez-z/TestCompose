package com.example.testCompose.presentation.injection

import android.content.Context
import com.example.testCompose.BuildConfig
import com.example.testCompose.common.LanguageDataStore
import com.example.testCompose.common.LanguageInterceptor
import com.example.testCompose.common.NetworkHandler
import com.example.testCompose.data.ServiceFactory
import com.example.testCompose.data.remote.Request
import com.example.testCompose.data.remote.datasource.MoviesRemoteDataSourceImpl
import com.example.testCompose.data.remote.datasource.MoviesRemoteDataSource
import com.example.testCompose.data.remote.api.ApiMovies
import com.example.testCompose.data.security.SecureStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteModule {

    @Binds
    @Singleton
    abstract fun bindRemoteDataSource(impl: MoviesRemoteDataSourceImpl): MoviesRemoteDataSource

    companion object {
        // Need @Provides — ServiceFactory, can't add @Inject
        @Provides
        @Singleton
        fun provideApiMovies(@ApplicationContext context: Context): ApiMovies =
            ServiceFactory.makeService(context, BuildConfig.DEBUG)

        // Need @Provides — Interceptor with @IntoSet
        @Provides
        @Singleton
        @IntoSet
        fun provideLanguageInterceptor(languageDataStore: LanguageDataStore): Interceptor {
            return LanguageInterceptor(languageDataStore)
        }
    }
}