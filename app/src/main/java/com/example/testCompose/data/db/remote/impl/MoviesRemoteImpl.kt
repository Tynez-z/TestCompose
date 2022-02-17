package com.example.testCompose.data.db.remote.impl

import android.util.Log
import com.example.testCompose.data.db.remote.core.Request
import com.example.testCompose.data.db.remote.remote.MoviesRemote
import com.example.testCompose.data.db.remote.service.ApiMovies
import com.example.testCompose.data.db.remote.service.ApiMovies.Companion.API_KEY
import com.example.testCompose.domain.entity.MoviesResponse
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.review.Reviews
import com.example.testCompose.domain.entity.search.SearchMovies
import com.example.testCompose.domain.entity.similarMovies.SimilarMovies
import com.example.testCompose.domain.entity.video.VideoList
import com.example.testCompose.domain.exception.Failure
import com.example.testCompose.domain.type.Either
import retrofit2.Response
import testCompose.BuildConfig
import javax.inject.Inject

class MoviesRemoteImpl @Inject constructor(
    private val request: Request,
    private val apiMovies: ApiMovies
) : MoviesRemote {

    override suspend fun getMovies(pageNumber: Int): Response<MoviesResponse> =
        apiMovies.getMovies(apiKey = BuildConfig.API_KEY, pageNumber = pageNumber)

    override suspend fun getSearchMovies(pageNumber: Int, query: String): Response<SearchMovies> =
        apiMovies.getSearchMovie(api_key = BuildConfig.API_KEY, pageNumber = pageNumber, query = query)

    override suspend fun getSimilarMovies(pageNumber: Int, movieId: Int): Response<SimilarMovies> =
        apiMovies.getSimilarMovies(apiKey = BuildConfig.API_KEY, pageNumber = pageNumber, movie_id = movieId)

    override fun getMovieDetails(movieId: Int): Either<Failure, MovieDetails> = request.make(apiMovies.getMovieDetails(movie_id = movieId, api_key = BuildConfig.API_KEY))

    override fun getMovieVideo(movieId: Int): Either<Failure, VideoList> {
        Log.i("AAAAAA", "getMovieVideo:${request.make(apiMovies.getMovieVideo(movie_id = movieId, api_key = BuildConfig.API_KEY))} ")
        return request.make(apiMovies.getMovieVideo(movie_id = movieId, api_key = BuildConfig.API_KEY))
    }

    override fun getReviews(movieId: Int): Either<Failure, Reviews> = request.make(apiMovies.getReviews(movie_id = movieId, api_key = BuildConfig.API_KEY))


//    override fun getMovies(): Either<Failure, MoviesResponse> {
//        return request.make(apiMovies.getMovies(apiKey = API_KEY, pageNumber = 1))
//    }


}