package com.example.testCompose.domain.interactor.useCase.cache

import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.actors.Actors
import com.example.testCompose.domain.entity.actors.MovieActors
import javax.inject.Inject

class InsertActorsUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend fun execute(actors: List<Actors>) = repository.insertActors(actors = actors)
}