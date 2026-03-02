package com.example.testCompose.data.local.datasource

import androidx.paging.PagingSource
import com.example.testCompose.data.local.dao.MoviesDao
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.entity.actors.Actors
import com.example.testCompose.domain.entity.actors.MovieActorsCrossRefTable
import com.example.testCompose.domain.entity.review.Result
import com.example.testCompose.domain.entity.savedMovies.SavedMovie
import com.example.testCompose.presentation.injection.DatabaseModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesLocalDataSourceImpl @Inject constructor(
    private val moviesDao: MoviesDao) : MoviesLocalDataSource {

    // Movies
    override fun getPagedMovies(): PagingSource<Int, Movies> =
        moviesDao.getPagedMovies()

    override suspend fun insertMovies(movies: List<Movies>) =
        moviesDao.insertMovies(movies)

    override suspend fun clearAllMovies() =
        moviesDao.clearAllMovies()

    override suspend fun hasMovies(): Boolean =
        moviesDao.hasMovies()

    // Favourites
    override fun getAllFavouriteMovies(): Flow<List<SavedMovie>> =
        flow { emitAll(moviesDao.getAllFavouriteMovies()) }

    override suspend fun addMovieToFavourites(movie: SavedMovie) =
        moviesDao.addMovieToFavourites(movie)

    override suspend fun removeMovieFromFavourites(movie: SavedMovie) =
        moviesDao.removeMovieFromFavourites(movie)

    // Actors
    override suspend fun insertActors(actors: List<Actors>) =
        moviesDao.insertActors(actors)

    override suspend fun insertMovieActorsCrossRef(
        roles: List<MovieActorsCrossRefTable>
    ) = moviesDao.insertMovieActorsCrossRef(roles)

    override fun getAllActorsFromDB(): Flow<List<Actors>> =
        flow { emitAll(moviesDao.getAllActorsFromDB()) }

    // Reviews
    override suspend fun insertReviews(reviews: List<Result>) =
        moviesDao.insertReviews(reviews)
}