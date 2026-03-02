package com.example.testCompose.domain.interactor.useCase.cache

import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.actors.MovieActorsCrossRefTable
import javax.inject.Inject

class InsertRoleUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend fun execute(roles: List<MovieActorsCrossRefTable>) = repository.insertRoles(roles)
}