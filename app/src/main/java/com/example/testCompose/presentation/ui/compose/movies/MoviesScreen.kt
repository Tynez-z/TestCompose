package com.example.testCompose.presentation.ui.compose.movies

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.testCompose.common.NetworkImage
import com.example.testCompose.common.PagingErrorMessage
import com.example.testCompose.common.PagingLoadItem
import com.example.testCompose.common.Resource
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.presentation.ui.compose.MainDestinations
import com.example.testCompose.presentation.ui.compose.savedMovies.SearchMoviesResults
import com.example.testCompose.presentation.viewModel.MovieDetailViewModel
import com.example.testCompose.presentation.viewModel.MoviesBottomSheetUiState
import com.example.testCompose.presentation.viewModel.MoviesViewModel
import com.example.testCompose.presentation.viewModel.SearchMovieViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
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

    var query by remember {
        mutableStateOf(TextFieldValue(searchViewModel.searchQuery.value))
    }

    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val uiState by moviesViewModel.uiState.collectAsState()

    val movieDetailViewModel = hiltViewModel<MovieDetailViewModel>()
    val uiStateDetail by movieDetailViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        moviesViewModel.getMoviesForBottomSheet()
        uiStateDetail.movieId?.let { id ->
            movieDetailViewModel.getMovieDetails(movieId = id) }
    }

    Scaffold(
        topBar = {
//            TopAppBar(
//                title = { Text(text = "Movies") }
//            )
        }, content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                    com.example.testCompose.presentation.ui.compose.savedMovies.TextField(
                        value = query,
                        onValueChange = { value ->
                            query = value
                            searchViewModel.updateQuery(value.text)
                        }, modifier = Modifier,
                        showSettingsDialog = showSettingsDialog
                    )

                searchViewModel.searchResults?.let {
                    SearchMoviesResults(
                        searchTerm = query.text,
                        searchResults = it,
                        navController = navController
                    )
                }

                ModalBottomSheetLayout(
                    sheetState = sheetState,
                    sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    sheetContent = {
                        Column(Modifier.defaultMinSize(minHeight = 1.dp)) {

                            uiStateDetail.movieDetailObject?.let { it1 ->
                                BottomSheetLayout(
                                    selectedMovie = it1,
                                    onMovieClick = { id->
                                        uiStateDetail.movieDetailObject?.id = id
                                        navController.navigate(MainDestinations.ArticleMoviesRoute.destination + "/${id}")
                                    },
                                    bottomSheetState = sheetState
                                )
                            }


//                            uiState.listOfMovies.let {
//                                when (val selectionMovie = moviesViewModel.selectedMovie) {
//                                    is Resource.Success -> {
//                                        selectionMovie.data?.let { selectedMovies ->
//                                            BottomSheetLayout(
//                                                selectedMovie = selectedMovies,
//                                                onMovieClick = { id ->
//                                                    moviesViewModel.setSelectedMovie(it.find { it.id == id })
//                                                    navController.navigate(MainDestinations.ArticleMoviesRoute.destination + "/${id}")
//                                                },
//                                                bottomSheetState = sheetState
//                                            )
//                                        }
//                                    }
//                                }
//                            }
                        }
                    }, content = {
                        MovieList(
                            movies = moviesViewModel.movies,
                            bottomSheetState = sheetState,
                            uiState = uiState,
                            moviesViewModel = moviesViewModel,
                            navController = navController,
                            detailViewModel = movieDetailViewModel
                        )
                    }
                )
//                MovieList(moviesViewModel.movies, navController)
            }
        }
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun MovieList(
    movies: Flow<PagingData<Movies>>,
    bottomSheetState: ModalBottomSheetState,
    detailViewModel: MovieDetailViewModel,
    moviesViewModel: MoviesViewModel,
    uiState: MoviesBottomSheetUiState,
    navController: NavController
) {
    val lazyMovieItems = movies.collectAsLazyPagingItems()

    val coroutineScope = rememberCoroutineScope()

    LazyVerticalGrid(cells = GridCells.Fixed(2), Modifier
        .background(Color.Black)
        .fillMaxSize()
        .padding(bottom = 60.dp),
        content = {
            items(lazyMovieItems.itemCount) { index ->
                lazyMovieItems[index]?.let {
                    MovieItem(movie = it, onItemClick = { movieId ->

                        detailViewModel.getMovieDetails(movieId = movieId.id)

//                        moviesViewModel.setSelectedMovie(uiState.listOfMovies.find { selectedMovie ->
//                            selectedMovie.id == movieId.id
//
//                        })
                        coroutineScope.launch {
                            bottomSheetState.show()
                        }

                        Log.i("AAAA", "MovieList:${movieId.id} ")
//                        navController.navigate("article/${it.id}")
//                        navController.navigate(MainDestinations.ArticleMoviesRoute.destination + "/${it.id}")
                    })
                }
            }
            lazyMovieItems.apply {
                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        item {
                            PagingLoadItem()
                        }
                        item {
                            PagingLoadItem()
                        }
                    }
                    is LoadState.Error -> {
                        val state = loadState.refresh as LoadState.Error
                        item {
                            PagingErrorMessage(
                                message = state.error.localizedMessage
                                    ?: "There was an error. Try again!"
                            )
                        }
                    }
                    LoadState.NotLoading(true) -> {
                        if (itemCount == 0) {
                            item {
                                PagingErrorMessage(
                                    message = "Try again!"
                                )
                            }
                        }
                    }
                }

                when (loadState.append) {
                    is LoadState.Loading -> {
                        item {
                            PagingLoadItem()
                        }
                        item {
                            PagingLoadItem()
                        }
                    }
                    is LoadState.Error -> {
                        val state = loadState.refresh as LoadState.Error
                        item {
                            PagingErrorMessage(
                                message = state.error.localizedMessage
                                    ?: "There was an error. Try again!"
                            )
                        }
                    }
                    LoadState.NotLoading(true) -> {
                        if (itemCount == 0) {
                            item {
                                PagingErrorMessage(
                                    message = "Try again!"
                                )
                            }
                        }
                    }
                }
            }

        })
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
            .padding(10.dp)
            .applyGradient(),
        contentScale = ContentScale.Crop,
//        placeHolder = R.drawable.placeholdericon,
        placeholder = ImageBitmap.imageResource(R.drawable.placeholdericon),
//        shimmerParams = ShimmerParams(
//            baseColor = Color.Red,
//            highlightColor = shimmerHighLightColor,
//            dropOff = 0.65f,
//            tilt = 20f,
//            durationMillis = 350
//        )


    )
}

@Composable
fun MovieTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        color = Color.White,
        style = MaterialTheme.typography.h2,
        modifier = modifier,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
//        overflow = TextOverflow.Ellipsis
    )
}

fun Modifier.applyGradient(): Modifier {
    return drawWithCache {
        val gradient = Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.Black),
            startY = size.height / 3,
            endY = size.height
        )
        onDrawWithContent {
            drawContent()
            drawRect(gradient, blendMode = BlendMode.Multiply)
        }
    }
}

@Composable
fun SpecificSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    contentColor: Color = colors.primary,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(elevation = elevation, shape = shape, clip = false)
            .zIndex(elevation.value)
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .clip(shape)
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor, content = content)
    }
}


@ExperimentalMaterialApi
@SuppressLint("UnrememberedMutableState")
@Composable
private fun BottomSheetLayout(
    selectedMovie: MovieDetails,
    onMovieClick: (Int) -> Unit,
    bottomSheetState: ModalBottomSheetState,
) {

    val coroutineScope = rememberCoroutineScope()

    SpecificSurface(
        shape = RoundedCornerShape(topStartPercent = 5, topEndPercent = 5),

        modifier = Modifier
            .wrapContentWidth()
            .height(350.dp)
            .background(Color.DarkGray)
            .clickable { onMovieClick(selectedMovie.id) }
    ) {
        SpecificSurface(
            shape = RoundedCornerShape(topStartPercent = 5, topEndPercent = 5),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column {
                Row(
                    modifier = Modifier.padding(
                        top = 12.dp,
                        start = 12.dp,
                        end = 12.dp,
                        bottom = 6.dp
                    )
                ) {
                    SmallMovieItem(movie = selectedMovie, onMovieSelected = onMovieClick)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = selectedMovie.title,
                                    fontSize = 20.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row {
                                    Text(
                                        text = selectedMovie.release_date.substring(0, 4),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        maxLines = 1
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = selectedMovie.vote_average.toString(),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        maxLines = 1
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = selectedMovie.vote_count.toString(),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        maxLines = 1
                                    )
                                }
                            }
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        bottomSheetState.hide()
                                    }
                                },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(25.dp)
                            ) {
                                Icon(
                                    tint = Color.White,
                                    imageVector = Icons.Outlined.Close,
                                    contentDescription = null
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = selectedMovie.overview,
                            fontSize = 14.sp,
                            color = Color.White,
                            maxLines = 4,
                            lineHeight = 18.sp,
                            overflow = Ellipsis
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.padding(
                        top = 6.dp,
                        start = 12.dp,
                        end = 12.dp,
                        bottom = 12.dp
                    )
                ) {
                    PlayButton(
                        isPressed = mutableStateOf(true),
                        modifier = Modifier.weight(2f)
                    )
                    IconTextButton(
                        buttonIcon = Icons.Outlined.Add,
                        buttonText = stringResource(id = R.string.download),
                        onButtonClick = {},
                        modifier = Modifier.weight(1f),
                    )
                    IconTextButton(
                        buttonIcon = Icons.Outlined.PlayArrow,
                        buttonText = stringResource(id = R.string.preview),
                        onButtonClick = {},
                        modifier = Modifier.weight(1f),
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color.Red, thickness = 1.dp)
                EpisodesAndInfo(
                    modifier = Modifier.padding(
                        top = 12.dp,
                        start = 12.dp,
                        end = 12.dp,
                        bottom = 12.dp
                    )
                )
            }
        }
    }
}

@Composable
fun SmallMovieItem(
    movie: MovieDetails, onMovieSelected: (Int) -> Unit,
) {
    Column {
        NetworkImage(
            networkUrl = BuildConfig.BASE_POSTER_PATH + movie.poster_path,
            modifier = Modifier
                .padding(10.dp)
                .height(130.dp)
                .width(80.dp)
                .clickable {
                    onMovieSelected(movie.id)
                }
                .applyGradient(),
            contentScale = ContentScale.Crop,
            placeholder = ImageBitmap.imageResource(R.drawable.placeholdericon),
        )
    }
}

@Composable
fun EpisodesAndInfo(modifier: Modifier) {
    Row(modifier = modifier) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = stringResource(id = R.string.episodesAndInfo),
            modifier = Modifier
                .weight(10f)
                .align(Alignment.CenterVertically)
                .padding(start = 4.dp),
            fontSize = 12.sp,
            color = Color.White,
            style = MaterialTheme.typography.button,
            maxLines = 1
        )
        Icon(
            imageVector = Icons.Outlined.ArrowForward,
            modifier = Modifier
                .weight(1f)
                .size(20.dp),
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
fun IconTextButton(
    buttonIcon: ImageVector,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.selectable(selected = false, onClick = { onButtonClick() })
    ) {
        Icon(
            imageVector = buttonIcon,
            tint = Color.White,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = buttonText,
            fontSize = 10.sp,
            color = Color.White,
            style = MaterialTheme.typography.button,
            maxLines = 1
        )
    }
}

@Composable
fun PlayButton(
    isPressed: MutableState<Boolean>,
    modifier: Modifier,
    cornerPercent: Int = 8
) {
    Button(
        onClick = { isPressed.value = isPressed.value.not() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White
        ),
        shape = RoundedCornerShape(cornerPercent),
        modifier = modifier
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.play),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.05).sp,
                color = Color.White,
                style = MaterialTheme.typography.button,
                maxLines = 1
            )
        }
    }
}

//@ExperimentalMaterialApi
//@Preview("Bottom Sheet Content")
//@Composable
//fun BottomSheetPreview() {
//    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
//    BottomSheetLayout(selectedMovie = movies.last(), onMovieClick = {}, bottomSheetState = sheetState)
//}
