package com.example.testCompose.domain.interactor.useCase

import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.review.Reviews
import com.example.testCompose.domain.exception.Failure
import com.example.testCompose.domain.interactor.UseCase
import com.example.testCompose.domain.type.Either
import javax.inject.Inject

class GetReviewsUseCase @Inject constructor(private val repository: MoviesRepository) :
    UseCase<Reviews, GetReviewsUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, Reviews> {
        return repository.getReviews(params.movieId)
    }

    data class Params(val movieId: Int)
}