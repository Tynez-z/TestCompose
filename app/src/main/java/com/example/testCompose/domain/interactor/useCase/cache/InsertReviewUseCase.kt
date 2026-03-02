package com.example.testCompose.domain.interactor.useCase.cache

import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.actors.MovieActorsCrossRefTable
import com.example.testCompose.domain.entity.review.Result
import javax.inject.Inject

class InsertReviewUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend fun execute(reviews: List<Result>) = repository.insertReview(reviews)
}