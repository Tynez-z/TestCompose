package com.example.testCompose.presentation.ui.compose.movies

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.testCompose.BuildConfig
import com.example.testCompose.common.PagingErrorMessage
import com.example.testCompose.common.PagingLoadItem
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.presentation.ui.compose.MainDestinations
import com.example.testCompose.presentation.ui.compose.components.BottomSheetLayout
import com.example.testCompose.presentation.ui.compose.components.NetworkImage
import com.example.testCompose.presentation.viewModel.MovieDetailViewModel
import com.example.testCompose.presentation.viewModel.MoviesViewModel
import com.example.testCompose.presentation.viewModel.SearchMovieViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import com.example.testCompose.R
import com.example.testCompose.presentation.viewModel.UiEvent


//@FlowPreview
//@ExperimentalFoundationApi
//@ExperimentalComposeUiApi
//@ExperimentalMaterialApi
//@Composable
//fun MoviesScreen(
//    navController: NavController,
//    scaffoldState: ScaffoldState,
//    moviesViewModel: MoviesViewModel = viewModel(),
//) {
//    MoviesScreen(
//        navController = navController,
//        scaffoldState = scaffoldState,
//        changeMenuState = { moviesViewModel.changeMenuState() })
//}

@SuppressLint("UnrememberedMutableState")
@ExperimentalMaterialApi
@FlowPreview
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun MoviesScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    moviesViewModel: MoviesViewModel = hiltViewModel(),
    changeMenuState: () -> Unit
) {
    val lazyMovies = moviesViewModel.moviesFlow.collectAsLazyPagingItems()

    val searchViewModel = hiltViewModel<SearchMovieViewModel>()

    val uiState by moviesViewModel.uiState.collectAsState()

    var query by remember {
        mutableStateOf(TextFieldValue(searchViewModel.searchQuery.value))
    }

    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val movieDetailViewModel = hiltViewModel<MovieDetailViewModel>()
    val uiStateDetail by movieDetailViewModel.uiState.collectAsState()

    val listState = rememberSaveable(saver = LazyGridState.Saver) { LazyGridState() }
    val context = LocalContext.current

    LaunchedEffect(uiStateDetail.movieId) {
        uiStateDetail.movieId?.let { id ->
            movieDetailViewModel.getMovieDetails(movieId = id)
        }
    }

    Scaffold(
        topBar = { },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                Row(
                    Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                ) {
                    Box {
                        OnClickSettings(
                            onSettingsClick = { changeMenuState() },
                            onFilterChanged = {
                                moviesViewModel.applyGenreFilter(it)
//                                moviesViewModel.loadMovies(it)
//                                moviesViewModel.getMovies(it)
                            }
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        Search(
                            value = query,
                            onValueChange = { value ->
                                query = value
                                searchViewModel.updateQuery(value.text)
                                moviesViewModel.onSubmitClicked()
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

                val pullToRefreshState = rememberPullToRefreshState()

                PullToRefreshBox(
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = {
//                        moviesViewModel.loadMovies(uiState.genreId)
                        lazyMovies.refresh()
                    },
                    state = pullToRefreshState,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    indicator = {
                        PullToRefreshDefaults.Indicator(
                            state = pullToRefreshState,
                            isRefreshing = uiState.isRefreshing,
                            modifier = Modifier.align(Alignment.TopCenter),
                            color = Color.Red
                        )
                    }
                ) {
                    Log.i("MovieScreen", "BottomSheet:${sheetState.isVisible} ")
                    ModalBottomSheetLayout(
                        sheetState = sheetState,
                        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        sheetBackgroundColor = Color.Black.copy(alpha = 0.7f),
                        sheetContent = {
                            Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
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
                                lazyMovies = lazyMovies,
                                bottomSheetState = sheetState,
                                movieDetails = { movieDetailViewModel.getMovieDetails(it) },
                                listState = listState
                            )
                        }
                    )
                }
            }
        }
    )
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun MovieList(
    lazyMovies: LazyPagingItems<Movies>,
    listState: LazyGridState,
    bottomSheetState: ModalBottomSheetState,
    movieDetails:(Int) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        state = listState,
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize(),
        content = {
            items(
                count = lazyMovies.itemCount,
                key = { index ->
                    lazyMovies[index]?.id ?: index
                }) { index ->
                lazyMovies[index]?.let { movie ->
                    MovieItem(
                        movie = movie,
                        onItemClick = { movieId ->
                            movieDetails(movieId.id)
                            coroutineScope.launch {
                                bottomSheetState.show()
                            }
                        }
                    )
                }
            }
        }
    )

    lazyMovies.apply {
        when (loadState.refresh) {
            is LoadState.Loading -> PagingLoadItem()
            is LoadState.Error -> {
                val state = loadState.refresh as LoadState.Error
                PagingErrorMessage(
                    message = state.error.localizedMessage
                        ?: stringResource(id = R.string.error_try_again)
                )
            }

            else -> {
                if (loadState.refresh is LoadState.NotLoading && itemCount == 0) {
                    PagingErrorMessage(message = stringResource(id = R.string.try_again))
                }
            }
        }

        when (loadState.append) {
            is LoadState.Loading -> PagingLoadItem()
            is LoadState.Error -> {
                val state = loadState.append as LoadState.Error
                PagingErrorMessage(
                    message = state.error.localizedMessage
                        ?: stringResource(id = R.string.error_try_again)
                )
            }

            else -> {}
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

@Composable
fun <T : Any> LazyPagingItems<T>.rememberLazyGridScrollState1(): LazyGridState {
    // After recreation, LazyPagingItems first return 0 items, then the cached items.
    // This behavior/issue is resetting the LazyListState scroll position.
    // Below is a workaround. More info: https://issuetracker.google.com/issues/177245496.
    return when (this.itemCount) {
        // Return a different LazyListState instance.
        0 -> remember(this) {
            LazyGridState()
        }
        // Return rememberLazyListState (normal case).
        else -> {
            rememberLazyGridState()
        }
    }
}

@Composable
fun <T : Any> LazyPagingItems<T>.rememberLazyListState2(): LazyListState {
    // After recreation, LazyPagingItems first return 0 items, then the cached items.
    // This behavior/issue is resetting the LazyListState scroll position.
    // Below is a workaround. More info: <https://issuetracker.google.com/issues/177245496>.
    return when (itemCount) {
        // Return a different LazyListState instance.
        0 -> remember(this) { LazyListState(0, 0) }
        // Return rememberLazyListState (normal case).
        else -> androidx.compose.foundation.lazy.rememberLazyListState()
    }
}