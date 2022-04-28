package com.example.testCompose.presentation.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.filter
import com.example.testCompose.common.paging.MoviePageSource
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.entity.genres.GenreItem
import com.example.testCompose.domain.interactor.useCase.GetGenresMovieUseCase
import com.example.testCompose.domain.interactor.useCase.GetMoviesUseCase
import com.example.testCompose.domain.type.None
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class MovieUiState(
    var movie: Flow<PagingData<Movies>>? = null,
    var genres: List<GenreItem>? = null,
    var isRefreshing: Boolean = false,
    var showDialog: Boolean = false,

    var genreId: Int? = null
)

@FlowPreview
@HiltViewModel
class MoviesViewModel @Inject constructor
    (
    @ApplicationContext val context: Context,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getGenresMovieUseCase: GetGenresMovieUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    fun getMovies(genreId: Int?) {
        if (genreId != null) {
            _uiState.update { movieUiState ->
                movieUiState.copy(movie = Pager(PagingConfig(10)) {
                    MoviePageSource(getMoviesUseCase)
                }.flow.map { pagingData ->
                    pagingData.filter {
                        it.genre_ids.contains(genreId)
                    }
                }, isRefreshing = false)
            }
        } else {
            _uiState.update { movieUiState ->
                movieUiState.copy(movie = Pager(PagingConfig(10)) {
                    MoviePageSource(getMoviesUseCase)
                }.flow, isRefreshing = false)
            }
        }
    }

    fun getGenres() {
        getGenresMovieUseCase(None()) {
            it.either(
                {},
                { listGenres ->
                    _uiState.update { movieUiState ->
                        movieUiState.copy(genres = listGenres.genres)
                    }
                }
            )
        }
    }

    fun changeMenuState() {
        _uiState.update { movieUiState ->
            when (uiState.value.showDialog) {
                true -> movieUiState.copy(showDialog = false)
                false -> movieUiState.copy(showDialog = true)
            }
        }
    }
}