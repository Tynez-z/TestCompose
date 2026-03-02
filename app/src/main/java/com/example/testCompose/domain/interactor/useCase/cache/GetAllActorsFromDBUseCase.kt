package com.example.testCompose.domain.interactor.useCase.cache

import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.actors.Actors
import com.example.testCompose.domain.entity.savedMovies.SavedMovie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllActorsFromDBUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend fun execute() : Flow<List<Actors>> = repository.getAllActorsFromDB()
}