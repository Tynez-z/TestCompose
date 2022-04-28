package com.example.testCompose.domain.interactor.useCase

import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.genres.Genres
import com.example.testCompose.domain.exception.Failure
import com.example.testCompose.domain.interactor.UseCase
import com.example.testCompose.domain.type.Either
import com.example.testCompose.domain.type.None
import javax.inject.Inject

class GetGenresMovieUseCase @Inject constructor(private val repository: MoviesRepository): UseCase<Genres, None>() {
    override suspend fun run(params: None): Either<Failure, Genres> = repository.getGenres()
}