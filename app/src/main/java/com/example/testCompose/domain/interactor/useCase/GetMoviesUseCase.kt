package com.example.testCompose.domain.interactor.useCase

import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.MoviesResponse
import com.example.testCompose.domain.exception.Failure
import com.example.testCompose.domain.interactor.UseCase
import com.example.testCompose.domain.type.Either
import com.example.testCompose.domain.type.None
import retrofit2.Response
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val repository: MoviesRepository) {
//    override suspend fun run(params: Params): Either<Failure, MoviesResponse> =
//        repository.getMovies(params.pageNumber)

    //    data class Params(val pageNumber : Int)
    suspend fun execute(pageNumber: Int): Response<MoviesResponse> =
        repository.getMovies(pageNumber)
}