package com.example.testCompose.presentation.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.exception.Failure
import com.example.testCompose.domain.interactor.useCase.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class MovieDetailUiState(
    var movieDetailObject: MovieDetails? = null,

    val movieId: Int? = null,

    val isLoadingProgressBar: Boolean = false,
    var isRefreshing: Boolean = false,
    val progressBarVisibility: Boolean = false,

    val isNetworkError: Boolean = false,

    val dialogTitle: String = "",
    val dialogMessage: String = ""
)


@HiltViewModel
class MovieDetailViewModel @Inject constructor
    (private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    val movieDetail: MutableState<MovieDetails?> = mutableStateOf(null)


    fun getMovieDetails(movieId: Int) {
        changeSwipeRefreshVisibility()
        changeProgressBarVisibility()

//        _uiState.update { movieDetailUiState ->
//            movieDetailUiState.copy(isRefreshing = true) }


        getMovieDetailsUseCase(GetMovieDetailsUseCase.Params(movieId = movieId)) {
            it.either({ failure ->
                when (failure) {
                    is Failure.NetworkConnectionError -> {
                        Log.i("AAAAA", "getMovieDetails: NO NETWORK CONNECTION ")
                        changeIsNetworkError()
                    }
                }
            },
                { details ->
//                    movieDetail.value = details
                    _uiState.update { uiState ->
                        uiState.copy(
                            movieDetailObject = details,
                            movieId = details.id
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