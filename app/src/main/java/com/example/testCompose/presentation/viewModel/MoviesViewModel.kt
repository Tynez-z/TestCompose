package com.example.testCompose.presentation.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.testCompose.common.paging.MoviePageSource
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.interactor.useCase.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class MovieUiState(
    var movie: Pager<Int, Movies>? = null,
    var isRefreshing: Boolean = false
)

@FlowPreview
@HiltViewModel
class MoviesViewModel @Inject constructor
    (
    @ApplicationContext val context: Context,
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    fun getMovies() {
        _uiState.update { movieUiState ->
            movieUiState.copy(movie = Pager(PagingConfig(10)) {
                MoviePageSource(getMoviesUseCase)
            }, isRefreshing = false)
        }
    }
}