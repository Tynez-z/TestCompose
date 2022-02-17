package com.example.testCompose.presentation.viewModel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.testCompose.common.MoviePageSource
import com.example.testCompose.common.MoviesUiState
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.entity.MoviesResponse
import com.example.testCompose.domain.exception.Failure
import com.example.testCompose.domain.interactor.useCase.GetMoviesUseCase
import com.example.testCompose.domain.type.HandleOnce
import com.example.testCompose.domain.type.None
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor
    (@ApplicationContext val context: Context,
    private val getMoviesUseCase: GetMoviesUseCase) : ViewModel() {

    val movies: Flow<PagingData<Movies>> = Pager(PagingConfig(10)) {
        MoviePageSource(getMoviesUseCase)
    }.flow
}