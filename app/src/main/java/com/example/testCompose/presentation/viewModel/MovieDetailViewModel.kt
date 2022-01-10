package com.example.testCompose.presentation.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.interactor.useCase.GetMovieDetailsUseCase
import com.example.testCompose.domain.interactor.useCase.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor
    (
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel() {

    val movieDetail: MutableState<MovieDetails?> = mutableStateOf(null)

    fun getMovieDetails(movieId: Int) {
        getMovieDetailsUseCase(GetMovieDetailsUseCase.Params(movieId = movieId)) {
            it.either({ failure ->
                Log.i("AAAAA", "getMovieDetails: FAIL")
            },
                { details ->
                    movieDetail.value = details
                }
            )
        }
    }

}