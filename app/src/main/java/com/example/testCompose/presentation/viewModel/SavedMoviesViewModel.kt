package com.example.testCompose.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.interactor.useCase.cache.GetAllFavouriteMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieSavedUiState(
    var movieList: List<MovieDetails>? = null
)

@HiltViewModel
class SavedMoviesViewModel @Inject constructor(private val getAllFavouriteMoviesUseCase: GetAllFavouriteMoviesUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow(MovieSavedUiState())
    val uiState: StateFlow<MovieSavedUiState> = _uiState.asStateFlow()

    init {
        getFavouriteMovieList()
    }

    private fun getFavouriteMovieList() {
        viewModelScope.launch {
            getAllFavouriteMoviesUseCase.execute()
                .onEach {}
                .catch {}
                .collect { listOfMovies ->
                    _uiState.update { movieSavedUiState ->
                        movieSavedUiState.copy(movieList = listOfMovies)
                    }
                }
        }
    }
}