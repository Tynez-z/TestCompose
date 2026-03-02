package com.example.testCompose.data.local.datasource

import androidx.paging.PagingSource
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.entity.actors.Actors
import com.example.testCompose.domain.entity.actors.MovieActorsCrossRefTable
import com.example.testCompose.domain.entity.review.Result
import com.example.testCompose.domain.entity.savedMovies.SavedMovie
import kotlinx.coroutines.flow.Flow

interface MoviesLocalDataSource {
    // Favourites
    suspend fun addMovieToFavourites(movies: SavedMovie)
    suspend fun removeMovieFromFavourites(movie: SavedMovie)
    fun getAllFavouriteMovies(): Flow<List<SavedMovie>>

    // Movies storage
     fun getPagedMovies(): PagingSource<Int, Movies>
    suspend fun insertMovies(movies: List<Movies>)
    suspend fun clearAllMovies()
    suspend fun hasMovies(): Boolean

    // Actors
//    suspend fun insertMovieActors(actors: MovieActors)
    suspend fun insertActors(actors: List<Actors>)
    suspend fun insertMovieActorsCrossRef(role: List<MovieActorsCrossRefTable>)
    fun getAllActorsFromDB(): Flow<List<Actors>>

    // Reviews
//    suspend fun insertMovieReviews(movieReviews: Reviews)
    suspend fun insertReviews(reviews: List<Result>)
}