package com.example.testCompose.domain.interactor.useCase

import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.similarMovies.SimilarMovies
import retrofit2.Response
import javax.inject.Inject

class GetSimilarMoviesUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend fun execute(pageNumber: Int, movieId: Int): Response<SimilarMovies> =
        repository.getSimilarMovies(pageNumber = pageNumber, movieId = movieId)
}