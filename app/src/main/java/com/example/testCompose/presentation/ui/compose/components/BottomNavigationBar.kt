package com.example.testCompose.presentation.ui.compose.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.get
import com.example.testCompose.presentation.ui.NavigationItem
import com.example.testCompose.presentation.ui.compose.appDestination
import com.example.testCompose.presentation.ui.compose.destinations.MoviesScreenDestination
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ramcosta.composedestinations.navigation.navigateTo
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalPagerApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(NavigationItem.Movies, NavigationItem.SavedMovies)
    val currentDestination = navController.currentBackStackEntryAsState().value?.appDestination()

    BottomNavigation(
        elevation = 5.dp,
        backgroundColor = Color.Black,
        modifier = Modifier.alpha(0.85f)
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = "") },
                label = { Text(text = item.title) },
                selected = currentDestination == item.route,
                onClick = {
//                    if (currentDestination != item.route) {
//                        navController.navigate(item.route) {
//                            navController.backQueue.clear()
//                        }
//                    }
                    navController.navigateTo(item.route) {
                        popUpTo(navController.graph[MoviesScreenDestination.route].id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                selectedContentColor = Color.Red,
                unselectedContentColor = Color.White,
                alwaysShowLabel = true
            )
        }
    }
}

@FlowPreview
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalPagerApi
@Preview(showBackground = true)
@Composable
fun PreviewBottomNavigationBar() {
    BottomNavigationBar(navController = rememberNavController())
}