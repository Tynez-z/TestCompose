package com.example.testCompose.data.db.remote.service

import androidx.annotation.IntRange
import com.example.testCompose.domain.entity.MoviesResponse
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.review.Reviews
import com.example.testCompose.domain.entity.video.VideoList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiMovies {

    companion object {
        const val GET_MOVIES = "/3/movie/popular"
        const val GET_MOVIE_DETAILS = "3/movie/{movie_id}"
        const val GET_MOVIE_VIDEO = "3/movie/{movie_id}/videos"
        const val GET_REVIEWS = "3/movie/{movie_id}/reviews"

        const val API_KEY = "api_key"
        const val PAGE = "page"
        const val TOTAL_PAGES = "total_pages"
        const val MAX_PAGE_SIZE = 10
        const val DEFAULT_PAGE_SIZE = 1
        const val MOVIE_ID = "movie_id"
    }

    @GET(GET_MOVIES)
    suspend fun getMovies(
        @Query(API_KEY) apiKey: String,
        @Query(PAGE) @IntRange(from = 1) pageNumber: Int,
        @Query(TOTAL_PAGES) @IntRange(from = 1, to = MAX_PAGE_SIZE.toLong()) totalPages: Int = DEFAULT_PAGE_SIZE
    ): Response<MoviesResponse>

    @GET(GET_MOVIE_DETAILS)
    fun getMovieDetails(
        @Path(MOVIE_ID) movie_id: Int,
        @Query(API_KEY) api_key: String): Call<MovieDetails>

    @GET(GET_MOVIE_VIDEO)
    fun getMovieVideo(@Path(MOVIE_ID) movie_id: Int, @Query(API_KEY)api_key: String) : Call<VideoList>

    @GET(GET_REVIEWS)
    fun getReviews(@Path(MOVIE_ID)movie_id: Int, @Query(API_KEY)api_key: String) : Call<Reviews>
}