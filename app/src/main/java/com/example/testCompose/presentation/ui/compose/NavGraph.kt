package com.example.testCompose.presentation.ui.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.testCompose.presentation.ui.compose.movies.ArticleScreen
import com.example.testCompose.presentation.ui.compose.movies.MoviesScreen
import com.example.testCompose.presentation.ui.compose.movies.SimilarMoviesScreen
import com.example.testCompose.presentation.ui.compose.savedMovies.SavedMoviesScreen
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.FlowPreview

sealed class MainDestinations(val destination: String) {
    object MoviesRoute : MainDestinations("movies")
    object SavedMoviesRoute : MainDestinations("saved_movies")
    object ArticleMoviesRoute : MainDestinations("article") {
        const val MOVIE_DETAIL_PATH = "/{movieItem}"
        const val MOVIE_ITEM = "movieItem"
    }
    object SimilarMoviesRoute : MainDestinations("similar") {
        const val SIMILAR_DETAIL = "/{movieItem}"
        const val SIMILAR_ITEM = "movieItem"
    }
}

@ExperimentalPagerApi
@ExperimentalMaterialApi
@FlowPreview
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun NavGraph(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    startDestinations: String = MainDestinations.MoviesRoute.destination
) {
    NavHost(navController = navController, startDestination = startDestinations) {
        composable(MainDestinations.MoviesRoute.destination) {
            MoviesScreen(navController, scaffoldState, hiltViewModel())
        }

        composable(MainDestinations.SavedMoviesRoute.destination) {
            SavedMoviesScreen(navController, scaffoldState)
        }

        composable(
            MainDestinations.ArticleMoviesRoute.destination.plus(MainDestinations.ArticleMoviesRoute.MOVIE_DETAIL_PATH),
            arguments = listOf(navArgument(MainDestinations.ArticleMoviesRoute.MOVIE_ITEM) {
                type = NavType.IntType
                nullable = false
            })
        ) {
            val movieItem = it.arguments?.getInt(
                MainDestinations.ArticleMoviesRoute.MOVIE_ITEM
            )
            if (movieItem != null) {
                ArticleScreen(navController, scaffoldState, movieId = movieItem, hiltViewModel())
            }
        }

        composable(
            MainDestinations.SimilarMoviesRoute.destination.plus(MainDestinations.SimilarMoviesRoute.SIMILAR_DETAIL),
            arguments = listOf(navArgument(MainDestinations.SimilarMoviesRoute.SIMILAR_ITEM) {
                type = NavType.IntType
                nullable = false
            })
        ) {
            val similarMovieItem =
                it.arguments?.getInt(MainDestinations.SimilarMoviesRoute.SIMILAR_ITEM)
            if (similarMovieItem != null) {
                SimilarMoviesScreen(navController, scaffoldState, movieId = similarMovieItem)
            }
        }
    }

//    if (showSettingsDialog.value) {
//        GenresContent {
//            showSettingsDialog.value = false
//        }
//    }
}
