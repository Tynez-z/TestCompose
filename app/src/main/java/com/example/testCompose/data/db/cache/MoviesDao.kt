package com.example.testCompose.data.db.cache

import androidx.room.*
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao : MoviesCache {

    @Query("SELECT * FROM movies")
    override fun getAllFavouriteMovies(): Flow<List<MovieDetails>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun addMovieToFavourites(movies: MovieDetails)

    @Delete
    override suspend fun removeMovieFromFavourites(movie: MovieDetails)
}