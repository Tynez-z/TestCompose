package com.example.testCompose.domain.entity.savedMovies

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteMovie")
data class SavedMovie(
    @PrimaryKey
    var id: Int,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: Double,
    val vote_count: Int
)