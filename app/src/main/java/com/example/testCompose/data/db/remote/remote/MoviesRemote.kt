package com.example.testCompose.data.db.remote.remote

import com.example.testCompose.domain.entity.MoviesResponse
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.genres.Genres
import com.example.testCompose.domain.entity.language.LanguageItem
import com.example.testCompose.domain.entity.language.Languages
import com.example.testCompose.domain.entity.review.Reviews
import com.example.testCompose.domain.entity.search.SearchMovies
import com.example.testCompose.domain.entity.similarMovies.SimilarMovies
import com.example.testCompose.domain.entity.video.VideoList
import com.example.testCompose.domain.exception.Failure
import com.example.testCompose.domain.type.Either
import retrofit2.Response

interface MoviesRemote {
    suspend fun getMovies(pageNumber: Int): Response<MoviesResponse>
    suspend fun getSearchMovies(pageNumber: Int, query: String): Response<SearchMovies>
    suspend fun getSimilarMovies(pageNumber: Int, movieId: Int): Response<SimilarMovies>
    fun getMovieDetails(movieId: Int): Either<Failure, MovieDetails>
    fun getMovieVideo(movieId: Int): Either<Failure, VideoList>
    fun getReviews(movieId: Int): Either<Failure, Reviews>
    suspend fun getLanguage(): Languages
    fun getGenres(): Either<Failure, Genres>
}