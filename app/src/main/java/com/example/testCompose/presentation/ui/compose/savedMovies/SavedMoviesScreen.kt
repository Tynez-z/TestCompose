package com.example.testCompose.presentation.ui.compose.savedMovies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun SavedMoviesScreen(navController: NavController, scaffoldState: ScaffoldState) {

    Column(modifier = Modifier.background(Color.White)) {
        Text(
            text = "SavedMoviesScreen",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SavedMoviePreview() {
    SavedMoviesScreen(rememberNavController(), rememberScaffoldState())
}