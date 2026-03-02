package com.example.testCompose.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie")
data class Movies(
    @PrimaryKey
    @ColumnInfo("movieId")
    val id: Int,
    val page: Int = 1,
    val title: String,
    val original_title: String,
    val vote_average: Double,
    val vote_count: Int,
    val release_date: String,
    val popularity: Double,
    val adult: Boolean,
    val backdrop_path: String? = null,
    val original_language: String,
    val genre_ids: List<Int>,
    val overview: String,
    val poster_path: String? = null,
    val video: Boolean,
    )