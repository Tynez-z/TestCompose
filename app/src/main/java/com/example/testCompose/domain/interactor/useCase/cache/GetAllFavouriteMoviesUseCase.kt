package com.example.testCompose.domain.interactor.useCase.cache

import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.savedMovies.SavedMovie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllFavouriteMoviesUseCase @Inject constructor(private val repository: MoviesRepository) {
    fun execute() : Flow<List<SavedMovie>> = repository.getAllFavoriteMovies()
}