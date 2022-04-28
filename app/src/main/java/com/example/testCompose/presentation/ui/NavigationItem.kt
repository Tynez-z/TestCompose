package com.example.testCompose.presentation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.testCompose.presentation.ui.compose.destinations.MoviesScreenDestination
import com.example.testCompose.presentation.ui.compose.destinations.SavedMoviesScreenDestination
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import kotlinx.coroutines.FlowPreview
import testCompose.R

@ExperimentalAnimationApi
@FlowPreview
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
sealed class NavigationItem(
    var route: DirectionDestinationSpec, var icon: Int, var title: String) {
    object Movies : NavigationItem(MoviesScreenDestination, R.drawable.ic_videoplayer, "Movies")
    object SavedMovies : NavigationItem(SavedMoviesScreenDestination, R.drawable.ic_save, "Saved Movies")
}