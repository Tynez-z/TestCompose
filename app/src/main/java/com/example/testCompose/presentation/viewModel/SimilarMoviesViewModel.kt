package com.example.testCompose.presentation.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.testCompose.common.MoviePageSource
import com.example.testCompose.common.SimilarMoviesPageSource
import com.example.testCompose.domain.entity.similarMovies.SimilarMoviesItems
import com.example.testCompose.domain.interactor.useCase.GetSimilarMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class SimilarMoviesUiState(
    var isRefreshing: Boolean = false
    )

@HiltViewModel
class SimilarMoviesViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SimilarMoviesUiState())
    val uiState: StateFlow<SimilarMoviesUiState> = _uiState.asStateFlow()

    private var movieId: Int? = null

    fun setMovieId(id: Int) {
        movieId = id

    }

    val movies: Flow<PagingData<SimilarMoviesItems>> = Pager(PagingConfig(2)) {
        SimilarMoviesPageSource(getSimilarMoviesUseCase, movieId = movieId!!)
    }.flow
}