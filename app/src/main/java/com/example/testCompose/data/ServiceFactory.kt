package com.example.testCompose.data

import android.content.Context
import com.example.testCompose.common.TIMEOUT_SEC
import com.example.testCompose.data.db.remote.service.ApiMovies
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import testCompose.R
import java.util.concurrent.TimeUnit

object ServiceFactory {

    fun makeService(context: Context, isDebug: Boolean): ApiMovies {
        val okHTTPClient = makeOkHTTPClient(
            makeLoggingInterceptor(isDebug)
        )

        return makeService(context, okHTTPClient, Gson())
    }

    private fun makeService(context: Context, okHttpClient: OkHttpClient, gson: Gson): ApiMovies {
        val retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.BASE_URL))
            .client(okHttpClient)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(ApiMovies::class.java)
    }

    private fun makeOkHTTPClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .build()
    }

    private fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        return logging
    }
}