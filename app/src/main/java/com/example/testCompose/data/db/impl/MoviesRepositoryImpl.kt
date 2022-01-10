package com.example.testCompose.data.db.impl

import com.example.testCompose.data.db.remote.remote.MoviesRemote
import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.MoviesResponse
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.review.Reviews
import com.example.testCompose.domain.entity.video.VideoList
import com.example.testCompose.domain.exception.Failure
import com.example.testCompose.domain.type.Either
import retrofit2.Response
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(private val moviesRemote: MoviesRemote) : MoviesRepository {
//    override fun getMovies(): Either<Failure, MoviesResponse> {
//        return moviesRemote.getMovies()
//    }

    override suspend fun getMovies(pageNumber: Int, totalPages: Int): Response<MoviesResponse> {
        return moviesRemote.getMovies(pageNumber, totalPages)
    }

    override fun getMovieDetails(movieId: Int): Either<Failure, MovieDetails> {
        return moviesRemote.getMovieDetails(movieId = movieId)
    }

    override fun getMovieVideo(movieId: Int): Either<Failure, VideoList> {
        return moviesRemote.getMovieVideo(movieId = movieId)
    }

    override fun getReviews(movieId: Int): Either<Failure, Reviews> =
        moviesRemote.getReviews(movieId = movieId)
}