package com.example.testCompose.domain.interactor.useCase

import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.MoviesResponse
import com.example.testCompose.domain.entity.search.SearchMovies
import retrofit2.Response
import javax.inject.Inject

class GetSearchMovieUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend fun execute(pageNumber: Int, query: String): Response<SearchMovies> =
        repository.getSearchMovies(pageNumber = pageNumber, query = query)
}