package com.example.testCompose.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testCompose.common.EMPTY_STRING
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.exception.Failure
import com.example.testCompose.domain.interactor.useCase.GetMovieDetailsUseCase
import com.example.testCompose.domain.interactor.useCase.cache.GetAllFavouriteMoviesUseCase
import com.example.testCompose.domain.interactor.useCase.cache.RemoveMovieFromFavouritesUseCase
import com.example.testCompose.domain.interactor.useCase.cache.SaveFavouriteMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieDetailUiState(
    var movieDetailObject: MovieDetails? = null,

    val movieId: Int? = null,

    val isLoadingProgressBar: Boolean = false,
    var isRefreshing: Boolean = false,
    val progressBarVisibility: Boolean = false,

    val isNetworkError: Boolean = false,

    val dialogTitle: String = EMPTY_STRING,
    val dialogMessage: String = EMPTY_STRING,
)

@HiltViewModel
class MovieDetailViewModel @Inject constructor
    (private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getAllFavouriteMoviesUseCase: GetAllFavouriteMoviesUseCase,
    private val removeMovieFromFavouritesUseCase: RemoveMovieFromFavouritesUseCase,
    private val saveFavouriteMovieUseCase: SaveFavouriteMovieUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    private val _favoritesFlow = MutableStateFlow(false)
    val favoritesFlow = _favoritesFlow.asStateFlow()

    fun favouriteMovie(movieId: Int) {
        viewModelScope.launch {
            getAllFavouriteMoviesUseCase.execute().collect { favourites ->
                _favoritesFlow.emit(favourites.any { movieDetails ->
                    movieDetails.id == movieId
                })
            }
        }
    }

    fun toggleFavourites(movie: MovieDetails) {
        viewModelScope.launch {
            if (favoritesFlow.value) {
                removeMovieFromFavouritesUseCase.execute(movie)
            } else {
                saveFavouriteMovieUseCase.execute(movie)
            }
        }
    }

    fun getMovieDetails(movieId: Int) {
        changeSwipeRefreshVisibility()
        changeProgressBarVisibility()

        getMovieDetailsUseCase(GetMovieDetailsUseCase.Params(movieId = movieId)) {
            it.either(
                { failure ->
                    when (failure) {
                        is Failure.NetworkConnectionError -> {
                            Log.i("AAAAA", "getMovieDetails: NO NETWORK CONNECTION ")
                            changeIsNetworkError()
                        }
                    }
                },
                { details ->
                    _uiState.update { uiState ->
                        uiState.copy(
                            movieDetailObject = details,
                            movieId = movieId
                        )
                    }
                }
            )
            changeSwipeRefreshVisibility()
            changeProgressBarVisibility()
        }
    }

    private fun changeSwipeRefreshVisibility() {
        _uiState.update { movieDetailUiState ->
            when (uiState.value.isRefreshing) {
                true -> movieDetailUiState.copy(isRefreshing = false)
                false -> movieDetailUiState.copy(isRefreshing = true)
            }
        }
    }

    private fun changeProgressBarVisibility() {
        _uiState.update { movieDetailUiState ->
            when (uiState.value.isLoadingProgressBar) {
                true -> movieDetailUiState.copy(isLoadingProgressBar = false)
                false -> movieDetailUiState.copy(isLoadingProgressBar = true)
            }
        }
    }

    fun changeIsNetworkError() {
        _uiState.update { movieDetailUiState ->
            when (uiState.value.isNetworkError) {
                true -> movieDetailUiState.copy(isNetworkError = false)
                false -> movieDetailUiState.copy(isNetworkError = true)
            }
        }
    }
}