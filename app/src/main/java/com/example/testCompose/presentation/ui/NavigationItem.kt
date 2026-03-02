package com.example.testCompose.presentation.ui

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import com.example.testCompose.R

sealed class NavigationItem(var route: String, @DrawableRes var icon: Int, var title: String) {
    object Movies : NavigationItem("movies", R.drawable.ic_videoplayer, "Movies")
    object SavedMovies : NavigationItem("saved_movies", R.drawable.ic_save, "Saved Movies")
    object Actors : NavigationItem("actors", R.drawable.rate, "Actors")

}