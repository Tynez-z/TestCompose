package com.example.testCompose.domain.exception

sealed class Failure {
    object NetworkConnectionError : Failure()
    object ServerError : Failure()
    object NullBody : Failure()
}