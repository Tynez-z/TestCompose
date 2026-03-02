package com.example.testCompose.presentation.ui.compose.components

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.testCompose.presentation.ui.NavigationItem

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem.Movies,
        NavigationItem.SavedMovies,
        NavigationItem.Actors
    )
    BottomNavigation(
        elevation = 5.dp,
        backgroundColor = Color.Black,
        modifier = Modifier.alpha(0.85f).systemBarsPadding()
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.route) },
                label = { Text(text = item.title) },
                selected = currentRoute == item.route,
                onClick = {
//                    if (currentRoute != item.route) {
//                        navController.navigate(item.route) {
//                            navController.backQueue.clear()
//                        }
//                    }
                    navController.navigate(item.route) {
                        // 1. Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // 2. Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true

                        // 3. Restore state when reselecting a previously selected item
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

@Preview(showBackground = true)
@Composable
fun PreviewBottomNavigationBar() {
    BottomNavigationBar(navController = rememberNavController())
}