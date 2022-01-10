package com.example.testCompose.presentation.injection

import android.content.Context
import com.example.testCompose.common.NetworkHandler
import com.example.testCompose.data.ServiceFactory
import com.example.testCompose.data.db.remote.core.Request
import com.example.testCompose.data.db.remote.impl.MoviesRemoteImpl
import com.example.testCompose.data.db.remote.remote.MoviesRemote
import com.example.testCompose.data.db.remote.service.ApiMovies
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import testCompose.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Provides
    @Singleton
    fun provideApiMovies(@ApplicationContext context: Context): ApiMovies =
        ServiceFactory.makeService(context, BuildConfig.DEBUG)

    @Provides
    @Singleton
    fun provideMoviesRemote(request: Request, apiServiceMovie: ApiMovies): MoviesRemote =
        MoviesRemoteImpl(request, apiServiceMovie)

    @Provides
    @Singleton
    fun provideNetworkHandler(@ApplicationContext context: Context) = NetworkHandler(context)
}