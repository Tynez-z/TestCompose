package com.example.testCompose.presentation.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.testCompose.common.paging.MoviePageSource
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.entity.genres.GenreItem
import com.example.testCompose.domain.interactor.useCase.GetGenresMovieUseCase
import com.example.testCompose.domain.interactor.useCase.GetMoviesUseCase
import com.example.testCompose.domain.interactor.useCase.cache.GetMoviesFromDBUseCase
import com.example.testCompose.domain.type.None
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieUiState(
//    var movie: Flow<PagingData<Movies>>? = null,
    var genres: List<GenreItem>? = null,
    var isRefreshing: Boolean = false,
    var showDialog: Boolean = false,

    var genreId: Int? = null
)

sealed class UiEvent {
    class ShowToast(val message: String) : UiEvent()
}

@FlowPreview
@HiltViewModel
class MoviesViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getGenresMovieUseCase: GetGenresMovieUseCase,
    private val getMoviesFromDBUseCase: GetMoviesFromDBUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _sharedFlow =
        MutableSharedFlow<UiEvent>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun onSubmitClicked() {
        viewModelScope.launch {
//            _events.send(UiEvent.ShowToast("Hey!"))
            Log.i("MovieViewModel", "onSubmitClicked:delay is started")
            delay(500)
//            _sharedFlow.emit(UiEvent.ShowToast("Hey"))
            repeat(5) { i ->
                _events.send(UiEvent.ShowToast("Event $i"))
                Log.i(
                    "MovieViewModel",
                    "onSubmitClicked-Sent event $i at ${System.currentTimeMillis()}"
                )
                delay(50) // One event every 50ms
            }
//            _events.send(UiEvent.ShowToast("Hey!"))
//            Log.i(
//                "MovieViewModel",
//                "onSubmitClicked:emitted value; bufferIsEmpty${_events.isEmpty} isClosedForReceive:${_events.isClosedForReceive} isClosedForSend:${_events.isClosedForSend} "
//            )
        }
    }

//    val uiStateTest : StateFlow<MovieUiState>
//        field = MutableStateFlow(MovieUiState())


    // Genre filter trigger
    private val genreFilter = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val moviesFlow: Flow<PagingData<Movies>> = genreFilter
        .flatMapLatest { genreId ->
            getMoviesFromDBUseCase()
                .map { pagingData ->
                    if (genreId != null) {
                        pagingData.filter { movie ->
                            movie.genre_ids.contains(genreId)
                        }
                    } else {
                        pagingData
                    }
                }
        }
        .cachedIn(viewModelScope)

    init {
        getGenres()
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

    fun applyGenreFilter(genreId: Int?) {
        genreFilter.value = genreId
        _uiState.update { it.copy(genreId = genreId) }
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