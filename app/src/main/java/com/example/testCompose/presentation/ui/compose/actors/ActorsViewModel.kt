package com.example.testCompose.presentation.ui.compose.actors

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testCompose.common.EMPTY_STRING
import com.example.testCompose.domain.entity.actors.Actors
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.savedMovies.SavedMovie
import com.example.testCompose.domain.interactor.useCase.cache.GetAllActorsFromDBUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ActorsUiState(
    var actorsList: List<Actors> = emptyList(),
)

@HiltViewModel
class ActorsViewModel @Inject constructor(private val getAllActorsFromDBUseCase: GetAllActorsFromDBUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow(ActorsUiState())
    val uiState: StateFlow<ActorsUiState> = _uiState.asStateFlow()

    init {
        getActorsList()
    }

    private fun getActorsList() {
        viewModelScope.launch {
            getAllActorsFromDBUseCase.execute()
                .onEach {}
                .catch {}
                .collect { actorsDB ->
                    _uiState.update {
                        it.copy(actorsList = actorsDB)
                    }
                }
        }
    }
}