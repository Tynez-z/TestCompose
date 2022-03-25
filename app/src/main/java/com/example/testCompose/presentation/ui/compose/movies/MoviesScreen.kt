package com.example.testCompose.presentation.ui.compose.movies

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.testCompose.common.PagingErrorMessage
import com.example.testCompose.common.PagingLoadItem
import com.example.testCompose.common.applyGradient
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.presentation.ui.compose.MainDestinations
import com.example.testCompose.presentation.ui.compose.components.BottomSheetLayout
import com.example.testCompose.presentation.ui.compose.components.MovieTitleText
import com.example.testCompose.presentation.ui.compose.components.NetworkImage
import com.example.testCompose.presentation.viewModel.MovieDetailViewModel
import com.example.testCompose.presentation.viewModel.MoviesViewModel
import com.example.testCompose.presentation.viewModel.SearchMovieViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.skydoves.landscapist.CircularReveal
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import testCompose.BuildConfig
import testCompose.R

@ExperimentalMaterialApi
@FlowPreview
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun MoviesScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    moviesViewModel: MoviesViewModel = viewModel(),
    showSettingsDialog: MutableState<Boolean>

) {
    val searchViewModel = hiltViewModel<SearchMovieViewModel>()

    val uiState by moviesViewModel.uiState.collectAsState()

    var query by remember {
        mutableStateOf(TextFieldValue(searchViewModel.searchQuery.value))
    }

    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val movieDetailViewModel = hiltViewModel<MovieDetailViewModel>()
    val uiStateDetail by movieDetailViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        uiStateDetail.movieId?.let { id ->
            movieDetailViewModel.getMovieDetails(movieId = id)
        }
        moviesViewModel.getMovies()
    }

    Scaffold(
        topBar = {},
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                TextField(
                    value = query,
                    onValueChange = { value ->
                        query = value
                        searchViewModel.updateQuery(value.text)
                    },
                    modifier = Modifier,
                    showSettingsDialog = showSettingsDialog,
                )

                SearchMoviesResults(
                    searchTerm = query.text,
                    searchResults = searchViewModel.searchResults,
                    navController = navController
                )

                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
                    onRefresh = {
                        moviesViewModel.getMovies()
                    },
                    indicatorAlignment = Alignment.TopCenter,
                    indicator = { state, trigger ->
                        SwipeRefreshIndicator(
                            state = state,
                            refreshTriggerDistance = trigger,
                            scale = true,
                            contentColor = Color.Red
                        )
                    }) {
                    ModalBottomSheetLayout(
                        sheetState = sheetState,
                        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        sheetBackgroundColor = Color.Black.copy(alpha = 0.7f),
                        sheetContent = {
                            Column(Modifier.defaultMinSize(minHeight = 1.dp)) {
                                BottomSheetLayout(
                                    selectedMovie = uiStateDetail.movieDetailObject,
                                    onMovieClick = { id ->
                                        uiStateDetail.movieDetailObject?.id = id
                                        navController.navigate(MainDestinations.ArticleMoviesRoute.destination + "/${id}")
                                    },
                                    bottomSheetState = sheetState
                                )
                            }
                        },
                        content = {
                            MovieList(
                                movies = uiState.movie,
                                bottomSheetState = sheetState,
                                detailViewModel = movieDetailViewModel
                            )
                        }
                    )
                }
            }
        }
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun MovieList(
    movies: Pager<Int, Movies>?,
    bottomSheetState: ModalBottomSheetState,
    detailViewModel: MovieDetailViewModel
) {
    if (movies != null) {
        val lazyMovieItems = movies.flow.collectAsLazyPagingItems()

        val coroutineScope = rememberCoroutineScope()

        LazyVerticalGrid(cells = GridCells.Fixed(2), Modifier
            .background(Color.Transparent)
            .fillMaxSize()
            .padding(bottom = 60.dp),
            content = {
                items(lazyMovieItems.itemCount) { index ->
                    lazyMovieItems[index]?.let {
                        MovieItem(movie = it, onItemClick = { movieId ->
                            detailViewModel.getMovieDetails(movieId = movieId.id)

                            coroutineScope.launch {
                                bottomSheetState.show()
                            }
                        })
                    }
                }
            })

        lazyMovieItems.apply {
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    PagingLoadItem()
                }
                is LoadState.Error -> {
                    val state = loadState.refresh as LoadState.Error
                    PagingErrorMessage(
                        message = state.error.localizedMessage
                            ?: stringResource(id = R.string.error_try_again)
                    )

                }
                LoadState.NotLoading(true) -> {
                    if (itemCount == 0) {
                        PagingErrorMessage(
                            message = stringResource(id = R.string.try_again)
                        )
                    }
                }
            }

            when (loadState.append) {
                is LoadState.Loading -> {
                    PagingLoadItem()
                }
                is LoadState.Error -> {
                    val state = loadState.refresh as LoadState.Error
                    PagingErrorMessage(
                        message = state.error.localizedMessage
                            ?: stringResource(id = R.string.error_try_again)
                    )
                }
                LoadState.NotLoading(true) -> {
                    if (itemCount == 0) {
                        PagingErrorMessage(
                            message = stringResource(id = R.string.try_again)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movies, onItemClick: (Movies) -> Unit) {
    Surface(
        color = Color.Transparent,
        contentColor = colors.onBackground
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
//            MovieTitle(title = movie.title)
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
            .padding(10.dp)
            .clip(RoundedCornerShape(18.dp)),
//            .applyGradient(),
        contentScale = ContentScale.Crop,
        placeholder = ImageBitmap.imageResource(R.drawable.film),
    )
}