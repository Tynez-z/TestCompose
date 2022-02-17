package com.example.testCompose.domain.entity

import androidx.room.Entity

@Entity(tableName = "moviesResponse")
data class MoviesResponse(
    val page: Int,
    val results: List<Movies>,
    val total_pages: Int,
    val total_results: Int
)