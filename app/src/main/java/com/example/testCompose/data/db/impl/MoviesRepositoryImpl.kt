package com.example.testCompose.data.db.impl

import com.example.testCompose.data.db.cache.MoviesCache
import com.example.testCompose.data.db.remote.remote.MoviesRemote
import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.MoviesResponse
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.language.Languages
import com.example.testCompose.domain.entity.review.Reviews
import com.example.testCompose.domain.entity.search.SearchMovies
import com.example.testCompose.domain.entity.similarMovies.SimilarMovies
import com.example.testCompose.domain.entity.video.VideoList
import com.example.testCompose.domain.exception.Failure
import com.example.testCompose.domain.type.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesRemote: MoviesRemote,
    private val moviesCache: MoviesCache
) : MoviesRepository {

    override fun getAllFavoriteMovies() = moviesCache.getAllFavouriteMovies()

    override suspend fun saveFavouriteMovie(movie: MovieDetails) =
        withContext(Dispatchers.IO) { moviesCache.addMovieToFavourites(movie) }

    override suspend fun removeMovieFromFavourites(movie: MovieDetails) =
        withContext(Dispatchers.IO) { moviesCache.removeMovieFromFavourites(movie) }


    override suspend fun getMovies(pageNumber: Int): Response<MoviesResponse> =
        withContext(Dispatchers.IO) { moviesRemote.getMovies(pageNumber) }

    override suspend fun getSearchMovies(pageNumber: Int, query: String): Response<SearchMovies> =
        moviesRemote.getSearchMovies(pageNumber = pageNumber, query = query)

    override suspend fun getSimilarMovies(pageNumber: Int, movieId: Int): Response<SimilarMovies> =
        withContext(Dispatchers.IO) { moviesRemote.getSimilarMovies(pageNumber = pageNumber, movieId = movieId) }

    override fun getMovieDetails(movieId: Int): Either<Failure, MovieDetails> =
        moviesRemote.getMovieDetails(movieId = movieId)

    override fun getMovieVideo(movieId: Int): Either<Failure, VideoList> =
        moviesRemote.getMovieVideo(movieId = movieId)

    override fun getReviews(movieId: Int): Either<Failure, Reviews> =
        moviesRemote.getReviews(movieId = movieId)

    override suspend fun getLanguage(): Languages {
        return moviesRemote.getLanguage()
    }
}