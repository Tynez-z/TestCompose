package com.example.testCompose.data.db.cache

import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MoviesCache {

    suspend fun addMovieToFavourites(movies: MovieDetails)
    suspend fun removeMovieFromFavourites(movie: MovieDetails)
    fun getAllFavouriteMovies(): Flow<List<MovieDetails>>
}