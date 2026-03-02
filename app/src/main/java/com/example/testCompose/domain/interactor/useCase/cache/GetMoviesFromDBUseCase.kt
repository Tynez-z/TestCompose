package com.example.testCompose.domain.interactor.useCase.cache

import androidx.paging.PagingData
import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.Movies
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesFromDBUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend operator fun invoke(): Flow<PagingData<Movies>> =
        repository.getPagedMovies()
}