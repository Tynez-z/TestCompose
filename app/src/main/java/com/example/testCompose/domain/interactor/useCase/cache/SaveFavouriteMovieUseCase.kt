package com.example.testCompose.domain.interactor.useCase.cache

import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.savedMovies.SavedMovie
import javax.inject.Inject

class SaveFavouriteMovieUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend fun execute(movie: SavedMovie) = repository.saveFavouriteMovie(movie)
}