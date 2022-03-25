package com.example.testCompose.domain.interactor.useCase.cache

import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import javax.inject.Inject

class RemoveMovieFromFavouritesUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend fun execute(movie: MovieDetails) = repository.removeMovieFromFavourites(movie)
}