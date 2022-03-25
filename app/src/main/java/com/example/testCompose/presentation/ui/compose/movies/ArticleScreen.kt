package com.example.testCompose.presentation.ui.compose.movies

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.testCompose.common.CircularProgressBar
import com.example.testCompose.common.convertDate
import com.example.testCompose.common.fromMinutesToHHmm
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.review.Result
import com.example.testCompose.domain.entity.video.Video
import com.example.testCompose.presentation.ui.compose.MainDestinations
import com.example.testCompose.presentation.ui.compose.components.NetworkImage
import com.example.testCompose.presentation.ui.compose.components.VideoPlayer
import com.example.testCompose.presentation.viewModel.MovieDetailUiState
import com.example.testCompose.presentation.viewModel.MovieDetailViewModel
import com.example.testCompose.presentation.viewModel.MovieVideoViewModel
import com.example.testCompose.presentation.viewModel.ReviewsViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import testCompose.BuildConfig
import testCompose.R

@SuppressLint("RememberReturnType")
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ArticleScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    movieId: Int,
    reviewViewModel: ReviewsViewModel = hiltViewModel(),
    movieDetailViewModel: MovieDetailViewModel = hiltViewModel()
) {
    val movieVideoViewModel = hiltViewModel<MovieVideoViewModel>()

    val uiState by movieDetailViewModel.uiState.collectAsState()
    val uiStateVideo by movieVideoViewModel.uiState.collectAsState()
    val uiStateReview by reviewViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        movieDetailViewModel.apply {
            getMovieDetails(movieId = movieId)
            favouriteMovie(movieId = movieId)
        }
        movieVideoViewModel.getVideo(movieId = movieId)
        reviewViewModel.getMovieReview(movieId = movieId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    uiState.movieDetailObject?.let { NameOfMovie(name = it.title) }
                },
                backgroundColor = Color.Transparent.copy(alpha = 1.0f),
                navigationIcon = {
                    OnClickBackButton(navController = navController)
                },
                actions = {
                    OnClickSaveMovie(
                        movie = uiState.movieDetailObject,
                        viewModel = movieDetailViewModel
                    )
                }
            )
        },
        content = {
            CircularProgressBar(uiState.isLoadingProgressBar)
            Column(
                Modifier
                    .fillMaxSize()
            ) {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
                    onRefresh = {
                        movieDetailViewModel.getMovieDetails(movieId)
                        movieVideoViewModel.getVideo(movieId)
                        reviewViewModel.getMovieReview(movieId)
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
                    MovieItemDetail(
                        movie = uiState.movieDetailObject,
                        movieVideo = uiStateVideo.videoList,
                        reviews = uiStateReview.reviewList,
                        navController = navController,
                        similarId = movieId
                    )
                }
                ShowError(
                    uiState = uiState,
                    onChangeNetworkNotificationStateClicked = { movieDetailViewModel.changeIsNetworkError() })
            }
        },
    )
}

@Composable
fun ShowError(
    uiState: MovieDetailUiState,
    onChangeNetworkNotificationStateClicked: () -> Unit,
) {
    com.example.testCompose.common.Dialog(
        showDialog = uiState.isNetworkError,
        title = stringResource(id = R.string.no_internet),
        text = stringResource(id = R.string.check_connection),
        confirmText = stringResource(id = R.string.OK),
        onChangeNotificationStateClicked = onChangeNetworkNotificationStateClicked
    )
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun MovieItemDetail(
    movie: MovieDetails?,
    movieVideo: List<Video>?,
    reviews: List<Result>?,
    navController: NavController,
    similarId: Int
) {
    val playingIndex = remember {
        mutableStateOf(0)
    }
    if (movie != null && movieVideo != null && reviews != null) {
        Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {
            MoviePoster(
                movie = movie, modifier = Modifier
                    .fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                VideoPlayer(
                    movieVideo = movieVideo,
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth(),
                    playingIndex = playingIndex
                )

                Spacer(modifier = Modifier.height(20.dp))

                MovieRelease(
                    release = "Release date: ${movie.release_date}",
                    reated = movie.vote_average,
                    runtime = movie.runtime.toLong(),
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.height(10.dp))

                MovieDescribe(title = movie.overview)

                Spacer(modifier = Modifier.height(10.dp))

                NameReviews()

                ShowReviews(reviews = reviews)

                Spacer(modifier = Modifier.height(10.dp))

                OnClickSimilarFilms(navController = navController, similarMoviesItems = similarId)

                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
fun MoviePoster(
    movie: MovieDetails,
    modifier: Modifier = Modifier
) {
    Box {
        NetworkImage(
            networkUrl = BuildConfig.BASE_BACKDROP_PATH + movie.backdrop_path,
            modifier = modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Surface(color = Color.Black.copy(alpha = 0.6f), modifier = Modifier.fillMaxSize()) {
        }
    }
}

@Composable
fun MovieDescribe(title: String) {
    Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
        Text(text = "Summary", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Justify,
            fontSize = 13.sp,
        )
    }
}

@Composable
fun MovieRelease(
    release: String,
    reated: Double,
    runtime: Long,
    modifier: Modifier
) {
    Row(
        modifier = Modifier.padding(start = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = release,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White, modifier = Modifier
                .border(1.dp, Color.White, RoundedCornerShape(5.dp))
                .padding(5.dp)
        )
        Text(
            text = "Rating: ${reated.toString()}",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White, modifier = Modifier
                .border(1.dp, Color.White, RoundedCornerShape(5.dp))
                .padding(5.dp)
        )
        Text(
            text = runtime.fromMinutesToHHmm(runtime),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier
                .border(1.dp, Color.White, RoundedCornerShape(5.dp))
                .padding(5.dp)
        )
    }
}

@Composable
private fun NameReviews() {
    Text(
        text = "Reviews",
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        modifier = Modifier.padding(start = 10.dp)
    )
}

@Composable
private fun ShowReviews(reviews: List<Result>?) {
    if (reviews?.isEmpty() == false) {
        Column {
            reviews.forEach { item ->
                ItemReview(reviewItem = item)
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "There are no reviews yet :(", fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun ItemReview(reviewItem: Result) {

    var expanded: Boolean by remember {
        mutableStateOf(false)
    }

    val iconAvatar = reviewItem.author_details.avatar_path?.replaceFirst("[/]".toRegex(), "".trim())

    Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
        Spacer(modifier = Modifier.height(10.dp))
        Row() {
            NetworkImage(
                networkUrl = iconAvatar,
                contentScale = ContentScale.Crop,
                placeholder = ImageBitmap.imageResource(R.drawable.placeholdericon),
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
            )
            Column(modifier = Modifier.padding(start = 8.dp), horizontalAlignment = Alignment.Start) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = reviewItem.author,
                        fontWeight = FontWeight.Bold,
                        color = Color.White, modifier = Modifier
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = reviewItem.created_at.convertDate(date = reviewItem.created_at),
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        fontSize = 10.sp,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        if (expanded) {
            Text(
                text = reviewItem.content,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
            )
        } else {
            Text(
                text = reviewItem.content,
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
            )
        }
    }
}

@Composable
fun OnClickSaveMovie(movie: MovieDetails?, viewModel: MovieDetailViewModel) {
    val isInFavorites = viewModel.favoritesFlow.collectAsState().value

    IconButton(
        onClick = {
            if (movie != null) {
                viewModel.toggleFavourites(movie)
            }
        }) {
        if (isInFavorites) {
            Image(
                imageVector = Icons.Default.Favorite,
                contentDescription = "",
                colorFilter = ColorFilter.tint(color = Color.Red)
            )
        } else {
            Image(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "",
                colorFilter = ColorFilter.tint(color = Color.White)
            )
        }
    }
}

@Composable
fun NameOfMovie(name: String) {
    Column(modifier = Modifier) {
        Text(
            text = name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
        )
    }
}

@Composable
fun OnClickBackButton(navController: NavController) {
    IconButton(onClick = {
        navController.navigate(MainDestinations.MoviesRoute.destination) {
            navController.popBackStack()
        }
    }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "",
            tint = Color.White
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun OnClickSimilarFilms(
    navController: NavController, similarMoviesItems: Int
) {
    Box() {
        OutlinedButton(
            onClick = {
                navController.navigate(MainDestinations.SimilarMoviesRoute.destination + "/${similarMoviesItems}")
            },
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.DarkGray.copy(alpha = 0.65f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Row() {
                Image(
                    painterResource(id = R.drawable.down),
                    contentDescription = "",
                    modifier = Modifier
                        .size(17.dp)
                        .padding(top = 3.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "More like this", fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}