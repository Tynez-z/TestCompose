package com.example.testCompose.presentation.ui.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.testCompose.presentation.ui.compose.components.BottomNavigationBar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ramcosta.composedestinations.DestinationsNavHost
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun MovieApp() {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) {
        DestinationsNavHost(
            navGraph = NavGraphs.root,
            navController = navController
        )
    }
}