package com.example.testCompose.presentation.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.testCompose.common.SearchMoviePageSource
import com.example.testCompose.common.SimilarMoviesPageSource
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.search.ResultSearchMovie
import com.example.testCompose.domain.entity.similarMovies.SimilarMoviesItems
import com.example.testCompose.domain.interactor.useCase.GetSearchMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class SearchMovieViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val getSearchMovieUseCase: GetSearchMovieUseCase
) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    var searchResults: Flow<PagingData<MovieDetails>>? by mutableStateOf(null)
    private var currentJob: Job? = null

    init {
        viewModelScope.launch {
            searchQuery.debounce(300).collectLatest { query ->
                currentJob?.cancel()
                currentJob = launch {
                    searchResults = if (query.isNotBlank()) search(query.trim()) else null
                }
            }
        }
    }

    fun updateQuery(query: String) {
        searchQuery.value = query
    }

    private fun search(query: String): Flow<PagingData<MovieDetails>> {
        return Pager(
            config = PagingConfig(2),
            pagingSourceFactory = {
                SearchMoviePageSource(
                    getSearchMovieUseCase = getSearchMovieUseCase,
                    query = query
                )
            }
        ).flow
    }
}