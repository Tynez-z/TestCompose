package com.example.testCompose.domain.entity.detailMovie

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies")
data class MovieDetails(
    val adult: Boolean,
    val backdrop_path: String,
//    val belongs_to_collection: BelongsToCollection,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String,
    @PrimaryKey
    @SerializedName("id")
    var id: Int,
    val imdb_id: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
//    val production_companies: List<ProductionCompany>,
//    val production_countries: List<ProductionCountry>,
    val release_date: String,
    val revenue: Double,
    val runtime: Int,
//    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)