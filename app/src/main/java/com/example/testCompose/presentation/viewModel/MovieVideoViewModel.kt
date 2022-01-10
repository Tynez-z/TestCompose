package com.example.testCompose.presentation.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.video.Video
import com.example.testCompose.domain.entity.video.VideoList
import com.example.testCompose.domain.interactor.useCase.GetMovieVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MovieVideoViewModel @Inject constructor(
    private val getMovieVideoUseCase: GetMovieVideoUseCase
) :
    ViewModel() {

//    private val _uiState = MutableStateFlow(MovieVideoUiState())
//    val uiState: StateFlow<MovieVideoUiState> = _uiState.asStateFlow()
//
//    init {
//        _uiState.update { videoUIState ->
//            videoUIState.copy(isLoading = true)
//        }
//
//        getMoviesVideo()
//    }
//
//    private fun getMoviesVideo() {
//        val movieId = _uiState.value.movieVideo?.id
//        _uiState.update { it.copy(isLoading = true) }
//
//        getMovieVideoUseCase(GetMovieVideoUseCase.Params(movieId = movieId)) {
//            it.either({ failure ->
//                Log.i("AAAAA", "getMoviesVideo: error $failure ")
//            },
//                { video ->
//                    _uiState.update { videoUIState ->
//                        videoUIState.copy(movieVideo = video, isLoading = false)
//                    }
//                }
//            )
//        }
//    }


//    fun getMovieVideo(movieId: Int) : MutableState<List<Video>>{
//        val trailers = mutableStateOf<List<Video>>(listOf())
//
//        val movieVideo: MutableState<Video?> = mutableStateOf(null)
//
//
//        getMovieVideoUseCase(GetMovieVideoUseCase.Params(movieId = movieId)) {
//            it.either({ failure ->
//                Log.i("AAAAA", "getMovieVideo: FAIL")
//            },
//                { video ->
//                    trailers.value = video.results
//                }
//            )
//        }
//        return trailers
//    }
    val movieVideo2: MutableState<List<Video>?> = mutableStateOf(null)

    fun getVideo(movieId: Int){
        val movieVideo: MutableState<VideoList?> = mutableStateOf(null)

        getMovieVideoUseCase(GetMovieVideoUseCase.Params(movieId = movieId)) {
            it.either({ failure ->
                Log.i("AAAAA", "getMovieVideo: FAIL")
            },
                { video ->
                    movieVideo2.value = video.results
                }
            )
        }
    }

    val movieVideo: MutableState<VideoList?> = mutableStateOf(null)

    fun getTestVideos(movieId: Int) {
        getMovieVideoUseCase(GetMovieVideoUseCase.Params(movieId = movieId)) {
            it.either({ failure ->
                Log.i("AAAAA", "getMovieVideo: FAIL")
            },
                { video ->
                    movieVideo.value = video
                }
            )
        }
    }
}