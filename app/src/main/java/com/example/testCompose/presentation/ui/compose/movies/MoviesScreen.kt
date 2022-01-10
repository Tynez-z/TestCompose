package com.example.testCompose.presentation.ui.compose.movies

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.testCompose.common.NetworkImage
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.presentation.ui.compose.MainDestinations
import com.example.testCompose.presentation.ui.theme.shimmerHighLightColor
import com.example.testCompose.presentation.viewModel.MoviesViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.skydoves.landscapist.ShimmerParams
import kotlinx.coroutines.flow.Flow
import testCompose.BuildConfig
import testCompose.R

@ExperimentalFoundationApi
@Composable
fun MoviesScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    moviesViewModel: MoviesViewModel = viewModel()
) {
    Scaffold(
        topBar = {
//            TopAppBar(
//                title = { Text(text = "Movies") }
//            )
        }, content = {
            MovieList(moviesViewModel.movies, navController)
        }
    )
}

@ExperimentalFoundationApi
@Composable
fun MovieList(movies: Flow<PagingData<Movies>>, navController: NavController) {
    val lazyMovieItems = movies.collectAsLazyPagingItems()

    LazyVerticalGrid(cells = GridCells.Fixed(2), state = rememberLazyListState(), modifier = Modifier.background(
        Color.Black),
        content = {
            items(lazyMovieItems.itemCount) { index ->
                lazyMovieItems[index]?.let {
                    MovieItem(movie = it, onItemClick = {
//                        navController.navigate("article/${it.id}")
                        navController.navigate(MainDestinations.ArticleMoviesRoute.destination + "/${it.id}")
                    })
                }
            }
        }

    )
}

@Composable
fun MovieItem(movie: Movies, onItemClick: (Movies) -> Unit) {
    Surface(
        color = Color.Black,
        contentColor = MaterialTheme.colors.onBackground
    ) {
        Column(
            Modifier
                .clip(RoundedCornerShape(4.dp))
                .clickable
                {
                    onItemClick.invoke(movie)
                }, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MovieImage(movie = movie)
            MovieTitle(
                title = movie.title, modifier = Modifier
            )
        }
    }
}

@Composable
fun MovieImage(
    movie: Movies
) {
    NetworkImage(
        networkUrl = BuildConfig.BASE_POSTER_PATH + movie.poster_path,
        modifier = Modifier
            .padding(10.dp),
        contentScale = ContentScale.Crop,
        shimmerParams = ShimmerParams(
            baseColor = MaterialTheme.colors.background,
            highlightColor = shimmerHighLightColor,
            dropOff = 0.65f,
            tilt = 20f,
            durationMillis = 350
        ),
    )
}

@Composable
fun MovieTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        color = Color.White,
        style = MaterialTheme.typography.h6,
        modifier = modifier,
        textAlign = TextAlign.Center
//        overflow = TextOverflow.Ellipsis
    )

}


@ExperimentalFoundationApi
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MoviePreview() {
    MoviesScreen(rememberNavController(), rememberScaffoldState(), hiltViewModel())
}