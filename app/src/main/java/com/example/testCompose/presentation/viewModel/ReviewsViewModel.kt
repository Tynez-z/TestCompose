package com.example.testCompose.presentation.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.testCompose.domain.entity.review.Result
import com.example.testCompose.domain.entity.video.Video
import com.example.testCompose.domain.interactor.useCase.GetReviewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class MovieReviewUiState(
    var reviewList: List<Result>? = null,
    val movieId: Int? = null
)

@HiltViewModel
class ReviewsViewModel @Inject constructor(private val reviewsUseCase: GetReviewsUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow(MovieReviewUiState())
    val uiState: StateFlow<MovieReviewUiState> = _uiState.asStateFlow()

    fun getMovieReview(movieId: Int) {
        reviewsUseCase(GetReviewsUseCase.Params(movieId = movieId)) {
            it.either({ failure ->
                Log.i("AAAAA", "getMovieVideo: FAIL")
            },
                { review ->
                    _uiState.update { movieReviewUiState ->
                        movieReviewUiState.copy(reviewList = review.results.sortedByDescending { it.created_at }
                        , movieId = movieId)
                    }
                }
            )
        }
    }
}