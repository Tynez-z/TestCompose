package com.example.testCompose.presentation.ui.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.testCompose.presentation.ui.compose.movies.ArticleScreen
import com.example.testCompose.presentation.ui.compose.movies.MoviesScreen
import com.example.testCompose.presentation.ui.compose.savedMovies.SavedMoviesScreen

sealed class MainDestinations(val destination: String) {
    object MoviesRoute : MainDestinations("movies")
    object SavedMoviesRoute : MainDestinations("saved_movies")
    object ArticleMoviesRoute : MainDestinations("article") {
        const val MOVIE_DETAIL_PATH = "/{movieItem}"
        const val MOVIE_ITEM = "movieItem"
    }
}

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
    }
}
