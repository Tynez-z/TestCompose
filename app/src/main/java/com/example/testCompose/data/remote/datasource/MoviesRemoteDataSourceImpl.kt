package com.example.testCompose.data.remote.datasource

import com.example.testCompose.BuildConfig
import com.example.testCompose.data.remote.Request
import com.example.testCompose.data.remote.api.ApiMovies
import com.example.testCompose.data.security.SecureStorage
import com.example.testCompose.data.security.model.SecureKey
import com.example.testCompose.domain.entity.MoviesResponse
import com.example.testCompose.domain.entity.actors.MovieActors
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.genres.Genres
import com.example.testCompose.domain.entity.language.Languages
import com.example.testCompose.domain.entity.review.Reviews
import com.example.testCompose.domain.entity.search.SearchMovies
import com.example.testCompose.domain.entity.similarMovies.SimilarMovies
import com.example.testCompose.domain.entity.video.VideoList
import com.example.testCompose.domain.exception.Failure
import com.example.testCompose.domain.type.Either
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRemoteDataSourceImpl @Inject constructor(
    private val request: Request,
    private val apiMovies: ApiMovies,
    private val storage: SecureStorage
) : MoviesRemoteDataSource {

    override suspend fun getMovies(pageNumber: Int): Response<MoviesResponse> {
        val apiKey = storage.get(SecureKey.API_KEY) ?: error("API key missing")
        return apiMovies.getMovies(apiKey = apiKey, pageNumber = pageNumber)
    }

    override suspend fun getSearchMovies(pageNumber: Int, query: String): Response<SearchMovies> =
        apiMovies.getSearchMovie(api_key = BuildConfig.API_KEY, pageNumber = pageNumber, query = query)

    override suspend fun getSimilarMovies(pageNumber: Int, movieId: Int): Response<SimilarMovies> =
        apiMovies.getSimilarMovies(apiKey = BuildConfig.API_KEY, pageNumber = pageNumber, movie_id = movieId)

    override fun getMovieDetails(movieId: Int): Either<Failure, MovieDetails> =
        request.make(apiMovies.getMovieDetails(movie_id = movieId, api_key = BuildConfig.API_KEY))

    override fun getMovieVideo(movieId: Int): Either<Failure, VideoList> {
        return request.make(apiMovies.getMovieVideo(movie_id = movieId, api_key = BuildConfig.API_KEY))
    }

    override fun getReviews(movieId: Int): Either<Failure, Reviews> =
        request.make(apiMovies.getReviews(movie_id = movieId, api_key = BuildConfig.API_KEY))

    override suspend fun getLanguage(): Languages {
//        Log.i("AAAAA", "getLanguage:${apiMovies.getLanguage(BuildConfig.API_KEY)} ")
        return apiMovies.getLanguage(BuildConfig.API_KEY)
    }

    override fun getGenres(): Either<Failure, Genres> {
        return request.make(apiMovies.getGenres(api_key = BuildConfig.API_KEY))
    }

    override suspend fun getMovieActors(movieId: Int): Either<Failure, MovieActors> {
        return request.make(apiMovies.getMovieActors(api_key = BuildConfig.API_KEY, movie_id = movieId))
    }
}