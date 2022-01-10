package com.example.testCompose.data.repository

import com.example.testCompose.domain.entity.MoviesResponse
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.review.Reviews
import com.example.testCompose.domain.entity.video.VideoList
import com.example.testCompose.domain.exception.Failure
import com.example.testCompose.domain.type.Either
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MoviesRepository {

    suspend fun getMovies(pageNumber: Int, totalPages: Int): Response<MoviesResponse>
    fun getMovieDetails(movieId: Int): Either<Failure, MovieDetails>
    fun getMovieVideo(movieId: Int): Either<Failure, VideoList>
    fun getReviews(movieId: Int): Either<Failure, Reviews>
}