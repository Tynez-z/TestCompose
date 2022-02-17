package com.example.testCompose.domain.entity.search

import com.example.testCompose.domain.entity.detailMovie.MovieDetails

data class SearchMovies(
    val page: Int,
    val results: List<MovieDetails>,
    val total_pages: Int,
    val total_results: Int
)