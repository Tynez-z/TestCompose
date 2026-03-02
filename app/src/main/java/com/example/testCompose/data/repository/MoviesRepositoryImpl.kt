package com.example.testCompose.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.testCompose.data.local.datasource.MoviesLocalDataSource
import com.example.testCompose.data.remote.datasource.MoviesRemoteDataSource
import com.example.testCompose.data.remote.mediator.MoviesRemoteMediator
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.entity.MoviesResponse
import com.example.testCompose.domain.entity.actors.Actors
import com.example.testCompose.domain.entity.actors.MovieActors
import com.example.testCompose.domain.entity.actors.MovieActorsCrossRefTable
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.genres.Genres
import com.example.testCompose.domain.entity.language.Languages
import com.example.testCompose.domain.entity.review.Result
import com.example.testCompose.domain.entity.review.Reviews
import com.example.testCompose.domain.entity.savedMovies.SavedMovie
import com.example.testCompose.domain.entity.search.SearchMovies
import com.example.testCompose.domain.entity.similarMovies.SimilarMovies
import com.example.testCompose.domain.entity.video.VideoList
import com.example.testCompose.domain.exception.Failure
import com.example.testCompose.domain.type.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepositoryImpl @Inject constructor(
    private val moviesRemote: MoviesRemoteDataSource,
    private val moviesLocal: MoviesLocalDataSource,
) : MoviesRepository {

    override fun getAllFavoriteMovies() = moviesLocal.getAllFavouriteMovies()

    override suspend fun saveFavouriteMovie(movie: SavedMovie) =
        moviesLocal.addMovieToFavourites(movie)

    override suspend fun removeMovieFromFavourites(movie: SavedMovie) =
        moviesLocal.removeMovieFromFavourites(movie)

    override suspend fun insertActors(actors: List<Actors>) {
        moviesLocal.insertActors(actors = actors)
    }

    override suspend fun insertRoles(role: List<MovieActorsCrossRefTable>) {
        moviesLocal.insertMovieActorsCrossRef(role)
    }

    override  fun getAllActorsFromDB(): Flow<List<Actors>> {
        return moviesLocal.getAllActorsFromDB()
    }

    override suspend fun insertReview(reviews: List<Result>) {
        return moviesLocal.insertReviews(reviews)
    }

    override suspend fun getMovies(pageNumber: Int): Response<MoviesResponse> =
       moviesRemote.getMovies(pageNumber)

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getPagedMovies(): Flow<PagingData<Movies>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 10,
                enablePlaceholders = false
            ),
            remoteMediator = MoviesRemoteMediator(moviesRemote = moviesRemote, moviesLocal = moviesLocal),
            pagingSourceFactory = { moviesLocal.getPagedMovies() }
        ).flow

    override suspend fun getSearchMovies(pageNumber: Int, query: String): Response<SearchMovies> =
        moviesRemote.getSearchMovies(pageNumber = pageNumber, query = query)

    override suspend fun getSimilarMovies(pageNumber: Int, movieId: Int): Response<SimilarMovies> =
         moviesRemote.getSimilarMovies(pageNumber = pageNumber, movieId = movieId)

    override fun getMovieDetails(movieId: Int): Either<Failure, MovieDetails> =
        moviesRemote.getMovieDetails(movieId = movieId)

    override fun getMovieVideo(movieId: Int): Either<Failure, VideoList> =
        moviesRemote.getMovieVideo(movieId = movieId)

    override fun getReviews(movieId: Int): Either<Failure, Reviews> {
        return moviesRemote.getReviews(movieId = movieId)
    }

    override suspend fun getLanguage(): Languages {
        return moviesRemote.getLanguage()
    }

    override  fun getGenres(): Either<Failure, Genres> {
        return moviesRemote.getGenres()
    }

    override suspend fun getMovieActors(movieId: Int): Either<Failure, MovieActors> {
        return moviesRemote.getMovieActors(movieId = movieId)
    }
}