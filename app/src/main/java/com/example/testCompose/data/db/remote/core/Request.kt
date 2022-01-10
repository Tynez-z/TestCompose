package com.example.testCompose.data.db.remote.core

import com.example.testCompose.common.NetworkHandler
import com.example.testCompose.domain.exception.Failure
import com.example.testCompose.domain.type.Either
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Request @Inject constructor(private val networkHandler: NetworkHandler) {

    fun <T> make(call: Call<T>): Either<Failure, T> {
        return when (networkHandler.isConnected) {
            true -> execute(call)
            false -> Either.Left(Failure.NetworkConnectionError)
        }
    }

    private fun <T> execute(call: Call<T>): Either<Failure, T> {
        return try {
            val response = call.execute()
            return when (response.isSuccessful) {
                true -> {
                    val body = response.body()
                    if (body != null) {
                        Either.Right(body)
                    } else {
                        Either.Left(Failure.NullBody)
                    }
                }
                false -> Either.Left(Failure.ServerError)
            }
        } catch (exception: Throwable) {
            Either.Left(Failure.ServerError)
        }
    }
}