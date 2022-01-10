package com.example.testCompose.presentation.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.testCompose.domain.entity.review.Result
import com.example.testCompose.domain.interactor.useCase.GetReviewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(private val reviewsUseCase: GetReviewsUseCase) :
    ViewModel() {

    fun getMovieReview(movieId: Int): MutableState<List<Result>> {
        val reviews = mutableStateOf<List<Result>>(listOf())

        val movieVideo: MutableState<Result?> = mutableStateOf(null)

        reviewsUseCase(GetReviewsUseCase.Params(movieId = movieId)) {
            it.either({ failure ->
                Log.i("AAAAA", "getMovieVideo: FAIL")
            },
                { review ->
                    reviews.value = review.results
                }
            )
        }
        return reviews
    }
}