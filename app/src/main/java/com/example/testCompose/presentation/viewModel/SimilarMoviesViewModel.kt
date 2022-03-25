package com.example.testCompose.presentation.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.testCompose.common.paging.SimilarMoviesPageSource
import com.example.testCompose.domain.entity.similarMovies.SimilarMoviesItems
import com.example.testCompose.domain.interactor.useCase.GetSimilarMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class SimilarMoviesUiState(
    var isRefreshing: Boolean = false,
    var similarMovies: Pager<Int, SimilarMoviesItems>? = null,
    var movieId: Int? = null
)

@HiltViewModel
class SimilarMoviesViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SimilarMoviesUiState())
    val uiState: StateFlow<SimilarMoviesUiState> = _uiState.asStateFlow()

    fun setMovieId(id: Int) {
        _uiState.update { similarMoviesUiState ->
            similarMoviesUiState.copy(isRefreshing = true)
        }

        _uiState.update { uiState ->
            uiState.copy(similarMovies = Pager(PagingConfig(2)) {
                SimilarMoviesPageSource(getSimilarMoviesUseCase, movieId = id)
            }, isRefreshing = false)
        }
    }
}