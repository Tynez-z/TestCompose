package com.example.testCompose.common

import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.entity.MoviesResponse

data class MoviesUiState(
    val isLoading: Boolean = false,
    val results: MoviesResponse? = null,
    val isError: String = ""
)
