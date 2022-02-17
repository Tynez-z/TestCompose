package com.example.testCompose.presentation.ui.compose.movies

import android.net.Uri
import android.util.Log
import android.util.SparseArray
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YtFile
import com.example.testCompose.common.CircularProgressBar
import com.example.testCompose.common.NetworkImage
import com.example.testCompose.common.fromMinutesToHHmm
import com.example.testCompose.data.db.remote.service.MovieVideo
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.review.Result
import com.example.testCompose.domain.entity.video.Video
import com.example.testCompose.presentation.ui.compose.MainDestinations
import com.example.testCompose.presentation.viewModel.MovieDetailUiState
import com.example.testCompose.presentation.viewModel.MovieDetailViewModel
import com.example.testCompose.presentation.viewModel.MovieVideoViewModel
import com.example.testCompose.presentation.viewModel.ReviewsViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import testCompose.BuildConfig
import testCompose.R

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

    val getReviews by remember {
        reviewViewModel.getMovieReview(movieId = movieId)
    }

    val movieVideoViewModel = hiltViewModel<MovieVideoViewModel>()
    val getVideo = movieVideoViewModel.movieVideo2.value


    val viewModel = hiltViewModel<MovieDetailViewModel>()
    val movieDetail = viewModel.movieDetail.value

    val uiState by movieDetailViewModel.uiState.collectAsState()

//    val similarMoviesViewModel = hiltViewModel<SimilarMoviesViewModel>()


    val playingIndex = remember {
        mutableStateOf(0)
    }

    LaunchedEffect(true) {
        viewModel.getMovieDetails(movieId = movieId)
        movieVideoViewModel.getVideo(movieId = movieId)
//        similarMoviesViewModel.setMovieId(id = movieId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                backgroundColor = Color.Transparent.copy(alpha = 1.0f),
                navigationIcon = {
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
//                        ImageVector.vectorResource(id = R.drawable.ic_backarrow)
                    }
                }
            )
        },
        content = {
            CircularProgressBar(uiState.isLoadingProgressBar)
            Column(
                Modifier
                    .fillMaxSize()
//                    .verticalScroll(rememberScrollState())
            ) {

//                VideoFilms(movieId = movieId)

                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
                    onRefresh = { movieDetailViewModel.getMovieDetails(movieId) },
                    indicatorAlignment = Alignment.TopCenter,
                    indicator = { state, trigger ->
                        SwipeRefreshIndicator(
                            state = state,
                            refreshTriggerDistance = trigger,
                            scale = true,
                            contentColor = Color.Red
                        )
                    }) {
                    if (getVideo != null) {
                        uiState.movieDetailObject?.let { it1 ->
                            MovieItemDetail(
                                movie = it1,
                                gameVideos = getVideo,
                                reviews = getReviews,
                                navController = navController,
                                similarId = movieId
                            )

                        }
                    }

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
        title = "No Internet",
        text = "Please, check your connection",
        confirmText = "OK",
        onChangeNotificationStateClicked = onChangeNetworkNotificationStateClicked
    )
}


@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun MovieItemDetail(
    movie: MovieDetails,
    gameVideos: List<Video>,
    reviews: List<Result>,
    navController: NavController,
    similarId: Int
) {
    val playingIndex = remember {
        mutableStateOf(0)
    }

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
                gameVideos = gameVideos,
//                playingIndex = playingIndex,
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth(),
                playingIndex = playingIndex
            )

            Spacer(modifier = Modifier.height(10.dp))

            NameOfMovie(name = movie.title, modifier = Modifier)

            Spacer(modifier = Modifier.height(10.dp))

            MovieRelease(
                release = "Release date: ${movie.release_date}",
                reated = movie.vote_average,
                runtime = movie.runtime.toLong(),
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(10.dp))

            MovieDescribe(title = movie.overview, modifier = Modifier)

            Spacer(modifier = Modifier.height(10.dp))

            NameReviews()

            ShowReviews(reviews = reviews)

            Spacer(modifier = Modifier.height(10.dp))

            TestBottomSheet()

            OnClickSimilarFilms(navController = navController, similarMoviesItems = similarId)

            Spacer(modifier = Modifier.height(60.dp))
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
fun MovieDescribe(title: String, modifier: Modifier) {
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
fun MovieRelease(release: String, reated: Double, runtime: Long, modifier: Modifier) {
    Row(modifier = Modifier.padding(start = 10.dp)) {
        Text(
            text = release,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White, modifier = Modifier
                .border(1.dp, Color.White, RoundedCornerShape(5.dp))
                .padding(5.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Rating: ${reated.toString()}",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White, modifier = Modifier
                .border(1.dp, Color.White, RoundedCornerShape(5.dp))
                .padding(5.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
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
fun NameOfMovie(name: String, modifier: Modifier) {
    Column(modifier = Modifier.padding(start = 10.dp)) {
        Text(
            text = name,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
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
private fun ShowReviews(reviews: List<Result>) {
    Column {
        reviews.forEach { item ->
            ItemReview(reviewItem = item)
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

        Row(modifier = Modifier) {
            NetworkImage(
                networkUrl = iconAvatar,
                contentScale = ContentScale.Crop,
                placeholder = ImageBitmap.imageResource(R.drawable.placeholdericon),
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = reviewItem.author,
                fontWeight = FontWeight.Bold,
                color = Color.White, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )

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

@ExperimentalMaterialApi
@Composable
fun TestBottomSheet() {
    val bottomSheetCoroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(scaffoldState = bottomSheetScaffoldState, sheetContent = {
        Column(
            Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .height(200.dp)
        ) {
            Text(text = "HELLO FROM SHEET", color = Color.Red)
        }
    }, sheetPeekHeight = 0.dp) {
        Button(onClick = {
            bottomSheetCoroutineScope.launch {
                if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                } else {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            }
        }) {
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

@ExperimentalMaterialApi
@Composable
private fun OnClickSimilarFilms(
    navController: NavController, similarMoviesItems: Int
) {

    val bottomSheetCoroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    Box() {
        OutlinedButton(
            onClick = {
                navController.navigate(MainDestinations.SimilarMoviesRoute.destination + "/${similarMoviesItems}")
            },
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.DarkGray.copy(alpha = 0.65f)),
//        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
//                .background(color = Color.Black),
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

@ExperimentalMaterialApi
fun onBottomSheetTapped(
    coroutineScope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState
) {
    coroutineScope.launch {
        try {
            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            } else {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        } catch (e: IllegalArgumentException) {
            Log.i("AAAA", "onBottomSheetTapped: ${e.message}")
        }
    }
}


//@ExperimentalAnimationApi
//@Composable
//private fun ListOfVideos(videos: List<Video>, modifier: Modifier, playingIndex: State<Int>) {
////    val playingIndex = remember {
////        mutableStateOf(0)
////    }
//    Column() {
//        videos.forEach { item ->
//            VideoPlayer(gameVideos = item, playingIndex = playingIndex)
//
//        }
//    }
//}


@ExperimentalAnimationApi
@Composable
private fun VideoPlayer(
    gameVideos: List<Video>,
    playingIndex: State<Int>,
    modifier: Modifier = Modifier

) {
    val context = LocalContext.current
    val visible = remember {
        mutableStateOf(true)
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build().apply {
                this.setHandleAudioBecomingNoisy(true)
            }
    }

//    gameVideos.forEach {

//    object : YouTubeExtractor(context) {
//        override fun onExtractionComplete(
//            ytFiles: SparseArray<YtFile>?,
//            videoMeta: VideoMeta?
//        ) {
//            if (ytFiles != null) {
//
//                val iTag = 137//tag of video 1080
//                val audioTag = 140 //tag m4a audio
//                // 720, 1080, 480
//                var videoUrl = ""
//                val iTags: List<Int> = listOf(22, 137, 18)
//                for (i in iTags) {
//                    val ytFile = ytFiles.get(i)
//                    if (ytFile != null) {
//                        val downloadUrl = ytFile.url
//                        if (downloadUrl != null && downloadUrl.isNotEmpty()) {
//                            videoUrl = downloadUrl
//                        }
//                    }
//                }
//                if (videoUrl == "")
//                    videoUrl = ytFiles[iTag].url
//                val audioUrl = ytFiles[audioTag].url
//                val audioSource: MediaSource = ProgressiveMediaSource
//                    .Factory(DefaultHttpDataSource.Factory())
//                    .createMediaSource(MediaItem.fromUri(audioUrl))
//                val videoSource: MediaSource = ProgressiveMediaSource
//                    .Factory(DefaultHttpDataSource.Factory())
//                    .createMediaSource(MediaItem.fromUri(videoUrl))
//                exoPlayer.setMediaSource(
//                    MergingMediaSource(true, videoSource, audioSource), true
//                )
//                exoPlayer.prepare()
//                exoPlayer.playWhenReady = true
////                exoPlayer.seekTo(currentWindow, playbackPosition)
//            }
//        }
//
//    }.extract(MovieVideo.getYoutubeVideoPath(gameVideos[0].key))


    try {
        object : at.huber.youtubeExtractor.YouTubeExtractor(context) {
            override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                videoMeta: VideoMeta?
            ) {
                val iTag = 18

                if (ytFiles?.get(iTag) != null && ytFiles.get(iTag).url != null) {

                    val downloadUrl = ytFiles.get(iTag).url

                    val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                        context, Util.getUserAgent(context, context.packageName)
                    )

                    val mediaSourcee = ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(downloadUrl))

                    exoPlayer.apply {
                        prepare(mediaSourcee, true, false)
                        playWhenReady = true
                    }
                } else {
                    exoPlayer.stop()
                    Log.i("AAA", "onExtractionFailed: ")
                }
            }
        }.extract(MovieVideo.getYoutubeVideoPath(gameVideos[0].key), true, true)

    } catch (e: IndexOutOfBoundsException) {
        OutlinedButton(
            onClick = {},
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.DarkGray.copy(alpha = 0.65f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
//                .background(color = Color.Black),
        ) {
            Text(
                text = "This video is unavailable", fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
        Log.i("AAAAAA", "VideoPlayer:$e ")
    }

    Log.i("AAAAAAA", "VideoPlayer: ${MovieVideo.getYoutubeVideoPath(gameVideos[0].key)}")

    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    exoPlayer.playWhenReady = false
                }
                Lifecycle.Event.ON_RESUME -> {
                    exoPlayer.playWhenReady = true
                }
                Lifecycle.Event.ON_DESTROY -> {
                    exoPlayer.run {
                        stop()
                        release()
                    }
                }
            }

        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = visible.value,
            enter = fadeIn(initialAlpha = 0.4f),
            exit = fadeOut(animationSpec = tween(durationMillis = 250))
        ) {}
        AndroidView(factory = {
            PlayerView(context).apply {
                player = exoPlayer
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        })
    }
}