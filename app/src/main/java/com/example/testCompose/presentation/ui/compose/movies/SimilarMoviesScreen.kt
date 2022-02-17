package com.example.testCompose.presentation.ui.compose.movies

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.testCompose.common.NetworkImage
import com.example.testCompose.domain.entity.similarMovies.SimilarMoviesItems
import com.example.testCompose.presentation.ui.theme.shimmerHighLightColor
import com.example.testCompose.presentation.viewModel.SimilarMoviesUiState
import com.example.testCompose.presentation.viewModel.SimilarMoviesViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.skydoves.landscapist.ShimmerParams
import kotlinx.coroutines.flow.Flow
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
            similarMovies = similarMoviesViewModel.movies,
            uiState = uiState,
            onRefresh = {})
    }
}


@ExperimentalFoundationApi
@Composable
fun ShowSimilarMovies(
    similarMovies: Flow<PagingData<SimilarMoviesItems>>,
    uiState: SimilarMoviesUiState,
    onRefresh: () -> Unit
) {
    val lazySimilarMovies = similarMovies.collectAsLazyPagingItems()

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
                            Column(
                                Modifier
                                    .clip(RoundedCornerShape(4.dp)),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                NetworkImage(
                                    networkUrl = BuildConfig.BASE_POSTER_PATH + it.poster_path,
                                    modifier = Modifier
                                        .padding(10.dp),
                                    contentScale = ContentScale.Crop,
                                )
                                Text(
                                    text = it.title,
                                    color = Color.White,
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}