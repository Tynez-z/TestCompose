package com.example.testCompose.presentation.ui.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.testCompose.presentation.ui.compose.components.BottomNavigationBar
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun MovieApp() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .systemBarsPadding()
//            .padding(paddingValues)
        ) {
            NavGraph(navController = navController, scaffoldState = scaffoldState)
        }
    }
}