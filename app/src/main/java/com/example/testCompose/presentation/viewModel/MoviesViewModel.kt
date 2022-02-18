package com.example.testCompose.presentation.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.testCompose.common.MoviePageSource
import com.example.testCompose.common.Resource
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.interactor.useCase.GetMoviesForBottomSheetUseCase
import com.example.testCompose.domain.interactor.useCase.GetMoviesUseCase
import com.example.testCompose.domain.type.None
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class MoviesBottomSheetUiState(
    val listOfMovies: List<Movies> = emptyList(),
    val idMovies: Int? = null
)


@HiltViewModel
class MoviesViewModel @Inject constructor
    (
    @ApplicationContext val context: Context,
    private val getMoviesForBottomSheetUseCase: GetMoviesForBottomSheetUseCase,
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    var selectedMovie by mutableStateOf<Resource<Movies?>>(Resource.Loading)

    private val _uiState = MutableStateFlow(MoviesBottomSheetUiState())
    val uiState: StateFlow<MoviesBottomSheetUiState> = _uiState.asStateFlow()


    val movies: Flow<PagingData<Movies>> = Pager(PagingConfig(10)) {
        MoviePageSource(getMoviesUseCase)
    }.flow

    val movieForBottomSheet: MutableState<List<Movies>?> = mutableStateOf(null)


    val movieBottomSheet: MutableState<Movies?> = mutableStateOf(null)

    var moviesList: List<Movies> = emptyList()
    fun getMoviesForBottomSheet() {

        val movies = moviesList.find {
            it.id == uiState.value.idMovies
        }

        getMoviesForBottomSheetUseCase(None()) {
            it.either(
                { failure ->
                    Log.i("AAAAA", "getMoviesForBottomSheetUseCase: FAIL")
                },
                { movie ->
                    this.moviesList = movie.results
                    _uiState.update { moviesBottomSheetUiState ->
                        moviesBottomSheetUiState.copy(
                            listOfMovies = movie.results
                        )
                    }

//                    movieForBottomSheet.value = movie.results
                }
            )
        }

    }


    fun setSelectedMovie(movie: Movies?) {
        selectedMovie = Resource.Success(movie)
    }

}