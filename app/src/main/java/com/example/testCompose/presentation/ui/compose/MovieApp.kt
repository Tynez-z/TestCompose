package com.example.testCompose.presentation.ui.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.testCompose.presentation.ui.NavigationItem
import com.example.testCompose.presentation.ui.compose.components.BottomNavigationBar

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun MovieApp() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    Scaffold(bottomBar = {
        BottomNavigationBar(navController) }
    ) {
        NavGraph(navController = navController, scaffoldState = scaffoldState)
    }
}