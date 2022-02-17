package com.example.testCompose.domain.entity.similarMovies

data class SimilarMovies(
    val page: Int,
    val results: List<SimilarMoviesItems>,
    val total_pages: Int,
    val total_results: Int
)