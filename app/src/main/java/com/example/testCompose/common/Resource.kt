package com.example.testCompose.common

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    object Loading : Resource<Nothing>()
}