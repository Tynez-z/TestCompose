package com.example.testCompose.presentation.ui

import androidx.annotation.DrawableRes
import testCompose.R

sealed class NavigationItem(var route: String, @DrawableRes var icon: Int, var title: String) {
    object Movies : NavigationItem("movies", R.drawable.ic_videoplayer, "Movies")
    object SavedMovies : NavigationItem("saved_movies", R.drawable.ic_save, "Saved Movies")
}