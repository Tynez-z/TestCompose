package com.example.testCompose.domain.interactor.useCase

import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.exception.Failure
import com.example.testCompose.domain.interactor.UseCase
import com.example.testCompose.domain.type.Either
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(private val repository: MoviesRepository) :
    UseCase<MovieDetails, GetMovieDetailsUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, MovieDetails> =
        repository.getMovieDetails(params.movieId)


    data class Params(val movieId: Int)
}