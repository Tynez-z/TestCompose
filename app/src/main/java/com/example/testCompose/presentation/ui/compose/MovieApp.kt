package com.example.testCompose.presentation.ui.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.rememberNavController
import com.example.testCompose.presentation.ui.compose.components.BottomNavigationBar
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun MovieApp(showSettingsDialog: MutableState<Boolean>) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    Scaffold(bottomBar = {
        BottomNavigationBar(navController)
    }
    ) {
        NavGraph(navController = navController, scaffoldState = scaffoldState, showSettingsDialog = showSettingsDialog)
    }
}