package com.example.testCompose.presentation.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.testCompose.domain.entity.video.Video
import com.example.testCompose.domain.entity.video.VideoList
import com.example.testCompose.domain.interactor.useCase.GetMovieVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class MovieVideoUiState(
    var videoList: List<Video>? = null,
    val movieId: Int? = null
)

@HiltViewModel
class MovieVideoViewModel @Inject constructor(
    private val getMovieVideoUseCase: GetMovieVideoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieVideoUiState())
    val uiState: StateFlow<MovieVideoUiState> = _uiState.asStateFlow()

    fun getVideo (movieId: Int) {
        getMovieVideoUseCase(GetMovieVideoUseCase.Params(movieId = movieId)) {
            it.either({ failure ->
                Log.i("AAAAA", "getMovieVideo: FAIL")
            },
                { video ->
                    _uiState.update { movieVideoUiState ->
                        movieVideoUiState.copy(videoList = video.results, movieId = movieId)
                    }
                }
            )
        }
    }
}