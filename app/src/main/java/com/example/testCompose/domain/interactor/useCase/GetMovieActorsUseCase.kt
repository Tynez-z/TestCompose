package com.example.testCompose.domain.interactor.useCase

import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.actors.MovieActors
import com.example.testCompose.domain.exception.Failure
import com.example.testCompose.domain.interactor.UseCase
import com.example.testCompose.domain.type.Either
import javax.inject.Inject

class GetMovieActorsUseCase @Inject constructor(private val repository: MoviesRepository): UseCase<MovieActors, GetMovieActorsUseCase.Params>() {
    override suspend fun run(params: Params): Either<Failure, MovieActors> {
        return repository.getMovieActors(movieId = params.movieId)
    }

    data class Params(val movieId: Int)
}