package com.example.testCompose.data.db.remote.service

import androidx.annotation.IntRange
import com.example.testCompose.domain.entity.MoviesResponse
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.genres.Genres
import com.example.testCompose.domain.entity.language.Languages
import com.example.testCompose.domain.entity.review.Reviews
import com.example.testCompose.domain.entity.search.SearchMovies
import com.example.testCompose.domain.entity.similarMovies.SimilarMovies
import com.example.testCompose.domain.entity.video.VideoList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiMovies {

    companion object {
        //        const val GET_MOVIES = "/3/movie/popular"
        const val GET_MOVIES = "/3/discover/movie"

        const val GET_MOVIE_DETAILS = "3/movie/{movie_id}"
        const val GET_MOVIE_VIDEO = "3/movie/{movie_id}/videos"
        const val GET_REVIEWS = "3/movie/{movie_id}/reviews"
        const val GET_SIMILAR_FILMS = "3/movie/{movie_id}/similar"
        const val GET_SEARCH_MOVIE = "3/search/movie"
        const val GET_MOVIE_GENRES = "3/genre/movie/list"

        const val GET_LANGUAGE = "3/configuration/languages"

        const val API_KEY = "api_key"
        const val PAGE = "page"
        const val QUERY = "query"
        const val MAX_PAGE_SIZE = 10

        const val MAX_PAGE_SIMILAR_MOVIE = 2
        const val DEFAULT_PAGE_SIZE = 1
        const val MOVIE_ID = "movie_id"
    }

    @GET(GET_MOVIES)
    suspend fun getMovies(
        @Query(API_KEY) apiKey: String,
        @Query(PAGE) @IntRange(from = 1, to = MAX_PAGE_SIZE.toLong()) pageNumber: Int = DEFAULT_PAGE_SIZE): Response<MoviesResponse>

    @GET(GET_SIMILAR_FILMS)
    suspend fun getSimilarMovies(
        @Path(MOVIE_ID) movie_id: Int,
        @Query(API_KEY) apiKey: String,
        @Query(PAGE) @IntRange(from = 1, to = MAX_PAGE_SIMILAR_MOVIE.toLong()) pageNumber: Int = DEFAULT_PAGE_SIZE): Response<SimilarMovies>

    @GET(GET_SEARCH_MOVIE)
    suspend fun getSearchMovie(
        @Query(API_KEY) api_key: String,
        @Query(QUERY) query: String,
        @Query(PAGE) @IntRange(from = 1, to = MAX_PAGE_SIMILAR_MOVIE.toLong()) pageNumber: Int = DEFAULT_PAGE_SIZE): Response<SearchMovies>

    @GET(GET_MOVIE_DETAILS)
    fun getMovieDetails(
        @Path(MOVIE_ID) movie_id: Int,
        @Query(API_KEY) api_key: String): Call<MovieDetails>

    @GET(GET_MOVIE_VIDEO)
    fun getMovieVideo(
        @Path(MOVIE_ID) movie_id: Int,
        @Query(API_KEY) api_key: String): Call<VideoList>

    @GET(GET_REVIEWS)
    fun getReviews(@Path(MOVIE_ID) movie_id: Int, @Query(API_KEY) api_key: String): Call<Reviews>

    @GET(GET_LANGUAGE)
    suspend fun getLanguage(@Query(API_KEY) api_key: String): Languages

    @GET(GET_MOVIE_GENRES)
    fun getGenres(@Query(API_KEY) api_key: String) : Call<Genres>
}