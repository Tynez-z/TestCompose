package com.example.testCompose.presentation.ui.compose.movies

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.Pager
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.testCompose.domain.entity.similarMovies.SimilarMoviesItems
import com.example.testCompose.presentation.ui.compose.MainDestinations
import com.example.testCompose.presentation.ui.compose.components.MovieTitleText
import com.example.testCompose.presentation.ui.compose.components.NetworkImage
import com.example.testCompose.presentation.viewModel.MoviesViewModel
import com.example.testCompose.presentation.viewModel.SearchMovieViewModel
import com.example.testCompose.presentation.viewModel.SimilarMoviesUiState
import com.example.testCompose.presentation.viewModel.SimilarMoviesViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.FlowPreview
import testCompose.BuildConfig
import testCompose.R

@FlowPreview
@ExperimentalFoundationApi
@Composable
fun SimilarMoviesScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    movieId: Int,
) {
    val searchViewModel = hiltViewModel<SearchMovieViewModel>()
    val moviesViewModel = hiltViewModel<MoviesViewModel>()

    val uiState by moviesViewModel.uiState.collectAsState()

    var query by remember {
        mutableStateOf(TextFieldValue(searchViewModel.searchQuery.value))
    }

    val similarMoviesViewModel = hiltViewModel<SimilarMoviesViewModel>()

    val uiStateSimilar by similarMoviesViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        moviesViewModel.apply {
            getGenres()
        }
        similarMoviesViewModel.setMovieId(id = movieId)
    }

    Column( modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {
        Row(
            Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Box {
                OnClickSettings(
                    onSettingsClick = { moviesViewModel.changeMenuState() },
                    onFilterChanged = {
                        TODO()
                    }
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                Search(
                    value = query, onValueChange = { value ->
                        query = value
                        searchViewModel.updateQuery(value.text)
                    }, modifier = Modifier.padding(end = 16.dp)
                )
            }
            Box(modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 10.dp)) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = stringResource(id = R.string.settings),
                    tint = Color.White,
                    modifier = Modifier
                )
            }
        }

        SearchMoviesResults(
            searchTerm = query.text,
            searchResults = searchViewModel.searchResults,
            navController = navController
        )

        ShowSimilarMovies(
            similarMovies = uiStateSimilar.similarMovies,
            uiState = uiStateSimilar,
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
            .clickable {
                onClick(movie.id)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NetworkImage(
            networkUrl = BuildConfig.BASE_POSTER_PATH + movie.poster_path,
            modifier = Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop,
        )
    }
}