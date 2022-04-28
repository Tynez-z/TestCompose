package com.example.testCompose.domain.entity

import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.genres.GenreItem

/**
 * Static apps data
 */

val movies = listOf(
  MovieDetails(
    id = 1,
    adult = false,
    backdrop_path = "https://image.tmdb.org/t/p/w500/pKAxHs04yxLDQSIf4MNiZoePVWX.jpg",
    poster_path = "https://image.tmdb.org/t/p/w500/auFsy7xWxLHGC3WrVyPEeKNVVUJ.jpg",
    original_language = "en-US",
    title = "Title of film.................................",
    overview = "Description of the film",
    popularity = 3116.245,
    release_date = "2021-05-26",
    video = false,
    vote_average = 8.8,
    vote_count = 1095,
    original_title = "original_title",
    budget = 1000,
    homepage = "",
    imdb_id = "",
    revenue = 1.1,
    runtime = 120,
    status = "status",
    tagline = "tagline",
    genres = listOf()
  )
)

val genresFake = listOf(GenreItem(id = 1, "sdshdgs"))
