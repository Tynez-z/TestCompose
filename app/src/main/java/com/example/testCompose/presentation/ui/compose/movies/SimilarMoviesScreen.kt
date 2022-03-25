package com.example.testCompose.presentation.ui.compose.movies

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.Pager
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.testCompose.presentation.ui.compose.components.NetworkImage
import com.example.testCompose.domain.entity.similarMovies.SimilarMoviesItems
import com.example.testCompose.presentation.ui.compose.MainDestinations
import com.example.testCompose.presentation.ui.compose.components.MovieTitleText
import com.example.testCompose.presentation.viewModel.SimilarMoviesUiState
import com.example.testCompose.presentation.viewModel.SimilarMoviesViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import testCompose.BuildConfig

@ExperimentalFoundationApi
@Composable
fun SimilarMoviesScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    movieId: Int,
) {
    val similarMoviesViewModel = hiltViewModel<SimilarMoviesViewModel>()

    val uiState by similarMoviesViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        similarMoviesViewModel.setMovieId(id = movieId)
    }

    Box() {
            ShowSimilarMovies(
                similarMovies = uiState.similarMovies,
                uiState = uiState,
                onRefresh = {
                    similarMoviesViewModel.setMovieId(movieId)
                },
                navController = navController
            )
    }
}


@ExperimentalFoundationApi
@Composable
fun ShowSimilarMovies(
    similarMovies: Pager<Int, SimilarMoviesItems>?,
    uiState: SimilarMoviesUiState,
    onRefresh: () -> Unit,
    navController: NavController
) {
    if (similarMovies != null) {
        val lazySimilarMovies = similarMovies.flow.collectAsLazyPagingItems()

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
            onRefresh = { onRefresh() }, indicatorAlignment = Alignment.TopCenter, indicator =
            { state, refreshTrigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = refreshTrigger,
                    scale = true,
                    contentColor = Color.Red
                )
            }) {
            LazyVerticalGrid(cells = GridCells.Fixed(3),
                state = rememberLazyListState(),
                modifier = Modifier.background(
                    Color.Black
                ),
                content = {
                    items(lazySimilarMovies.itemCount) { index ->
                        lazySimilarMovies[index]?.let {
                            Surface(
                                color = Color.Black,
                                contentColor = MaterialTheme.colors.onBackground
                            ) {
                                SimilarMovieItem(movie = it, onClick = { id ->
                                    navController.navigate(MainDestinations.ArticleMoviesRoute.destination + "/${it.id}")
                                })
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun SimilarMovieItem(movie: SimilarMoviesItems, onClick: (Int) -> Unit) {
    Column(
        Modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable {
                onClick(movie.id)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NetworkImage(
            networkUrl = BuildConfig.BASE_POSTER_PATH + movie.poster_path,
            modifier = Modifier
                .padding(10.dp),
            contentScale = ContentScale.Crop,
        )
        MovieTitleText(
            text = movie.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}