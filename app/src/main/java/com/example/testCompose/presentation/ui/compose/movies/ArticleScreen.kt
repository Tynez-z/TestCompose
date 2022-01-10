package com.example.testCompose.presentation.ui.compose.movies

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.util.SparseArray
import android.view.RoundedCorner
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.*
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.example.testCompose.common.NetworkImage
import com.example.testCompose.data.db.remote.service.MovieVideo
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.review.Result
import com.example.testCompose.domain.entity.review.Reviews
import com.example.testCompose.domain.entity.video.Video
import com.example.testCompose.domain.entity.video.VideoList
import com.example.testCompose.presentation.ui.compose.MainDestinations
import com.example.testCompose.presentation.viewModel.MovieDetailViewModel
import com.example.testCompose.presentation.viewModel.MovieVideoViewModel
import com.example.testCompose.presentation.viewModel.ReviewsViewModel
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import testCompose.BuildConfig

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ArticleScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    movieId: Int,
    reviewViewModel: ReviewsViewModel = hiltViewModel()
) {

//    val uiState by moviesViewModel.uiState.collectAsState()

//    val trailerList by remember {
//        moviesViewModel.getMovieVideo(movieId = movieId)
//    }

//    val getVideo by remember {
//        moviesViewModel.getVideo(movieId = movieId)
//    }

    val getReviews by remember {
        reviewViewModel.getMovieReview(movieId = movieId)
    }

    val movieVideoViewModel = hiltViewModel<MovieVideoViewModel>()
    val getVideo = movieVideoViewModel.movieVideo2.value


    val viewModel = hiltViewModel<MovieDetailViewModel>()
    val movieDetail = viewModel.movieDetail.value

    val playingIndex = remember {
        mutableStateOf(0)
    }

    LaunchedEffect(true) {
        viewModel.getMovieDetails(movieId = movieId)
        movieVideoViewModel.getVideo(movieId = movieId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                backgroundColor = Color.Transparent.copy(alpha = 1.0f),

                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(MainDestinations.MoviesRoute.destination)

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
            Column(
                Modifier
                    .fillMaxSize()
//                    .verticalScroll(rememberScrollState())
            ) {
                
                VideoFilms(movieId = movieId)
//                if (movieDetail != null && getVideo != null) {
//                    MovieItemDetail(
//                        movie = movieDetail,
//                        gameVideos = getVideo, reviews = getReviews
//                    )
//                }
            }
        },
    )

}

@ExperimentalAnimationApi
@Composable
fun MovieItemDetail(movie: MovieDetails, gameVideos: List<Video>, reviews: List<Result>) {
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
                    .padding(start = 10.dp, end = 10.dp)
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
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(10.dp))

            MovieDescribe(title = movie.overview, modifier = Modifier)

            Spacer(modifier = Modifier.height(10.dp))

            NameReviews()

            ShowReviews(reviews = reviews)
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
fun MovieRelease(release: String, reated: Double, modifier: Modifier) {
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
        Spacer(modifier = Modifier.height(60.dp))
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
            NetworkImage(networkUrl = iconAvatar, contentScale = ContentScale.Crop, modifier = Modifier
                .width(30.dp)
                .height(30.dp))
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


@Composable
private fun VideoThumbnail(trailers: List<Video>, context: Context, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
    ) {
        Text(
            text = "Trailers",
            style = TextStyle(Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        )
        LazyRow {
            items(trailers.size) {
                Box(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable {

                            val playVideoIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(MovieVideo.getYoutubeVideoPath(trailers[it].key))
                            )
                            context.startActivity(playVideoIntent)

                        },
                    contentAlignment = Alignment.Center
                ) {

                    NetworkImage(
                        networkUrl = MovieVideo.getYoutubeThumbnailPath(trailers[it].key),
                        modifier = Modifier
                            .width(120.dp)
                            .height(90.dp)

                    )

                    NetworkImage(
                        networkUrl = MovieVideo.getYoutubeVideoPath(trailers[it].key),
                        modifier = Modifier
                            .width(120.dp)
                            .height(90.dp)
                    )
                }
            }
        }
    }
}


//@ExperimentalAnimationApi
//@Composable
//fun ShowMovieVideos(gameVideos: VideoList) {
//    val playingIndex = remember {
//        mutableStateOf(0)
//    }
//
//    fun onTrailerChange(index: Int) {
//        playingIndex.value = index
//    }
//
//    Column {
//        gameVideos.results?.let {
//            ListOfVideos(
//                modifier = Modifier.weight(1f, fill = true),
//                videos = it,
//                playingIndex = playingIndex,
//                onTrailerChange = { newIndex -> onTrailerChange(newIndex) }
//            )
//        }
//        LazyColumn(
//            modifier = Modifier.weight(1f, fill = true),
//            content = {
//                itemsIndexed(gameVideos.results) { index, trailer ->
//                    ShowTrailers(
//                        index = index,
//                        trailer = trailer,
//                        playingIndex = playingIndex,
//                        onTrailerClicked = { newIndex -> onTrailerChange(newIndex) })
//                }
//            })
//    }
//}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ShowTrailers(
    index: Int,
    trailer: Video,
    playingIndex: State<Int>,
    onTrailerClicked: (Int) -> Unit
) {
    val currentlyPlaying = remember {
        mutableStateOf(false)
    }
    currentlyPlaying.value = index == playingIndex.value
    ConstraintLayout(modifier = Modifier
        .testTag("TrailerParent")
        .padding(8.dp)
        .wrapContentSize()
        .clickable {
            onTrailerClicked(index)
        }) {
        val (thumbnail, play, title, nowPlaying) = createRefs()

        if (currentlyPlaying.value) {

        }
        Text(
            text = trailer.name,
            color = Color.White,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(thumbnail.top, margin = 8.dp)
                    start.linkTo(thumbnail.end, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    width = Dimension.preferredWrapContent
                    height = Dimension.wrapContent
                },
        )
        if (currentlyPlaying.value) {
            Text(
                text = "",
                modifier = Modifier.constrainAs(nowPlaying) {
                    top.linkTo(title.bottom, margin = 8.dp)
                    start.linkTo(thumbnail.end, margin = 8.dp)
                    bottom.linkTo(thumbnail.bottom, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    width = Dimension.preferredWrapContent
                    height = Dimension.preferredWrapContent
                }
            )
        }
        TrailerDivider()
    }
}

@Composable
fun TrailerDivider() {
    Divider(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .testTag("Divider"),
    )
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
    val videoTitle = remember {
        mutableStateOf(gameVideos[playingIndex.value].name)
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build().apply {
                this.setHandleAudioBecomingNoisy(true)
//                this.addListener(object : Player.Listener {
//                    override fun onEvents(player: Player, events: Player.Events) {
//                        super.onEvents(player, events)
//                        if (player.contentPosition >= 200) visible.value = false
//                    }
//
//                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
//                        super.onMediaItemTransition(mediaItem, reason)
//                        onTrailerClicked(this@apply.currentPeriodIndex)
//                        visible.value = true
//                        videoTitle.value = mediaItem?.mediaMetadata?.displayTitle.toString()
//                    }
//                })
            }
    }

//    gameVideos.forEach {
    object : at.huber.youtubeExtractor.YouTubeExtractor(context) {
        override fun onExtractionComplete(
            ytFiles: SparseArray<at.huber.youtubeExtractor.YtFile>?,
            videoMeta: at.huber.youtubeExtractor.VideoMeta?
        ) {
            val iTag = 18

            if (ytFiles?.get(iTag) != null && ytFiles.get(iTag).url != null) {

                val donwloadUrl = ytFiles.get(iTag).url

                val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                    context,
                    Util.getUserAgent(context, context.packageName)
                )

                val mediaSourcee = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(donwloadUrl))

                exoPlayer.prepare(mediaSourcee, true, false)
                exoPlayer.playWhenReady = true

            } else {
                exoPlayer.stop()
                Log.i("AAAAA", "onExtractionFailed: ")
            }
        }
    }.extract(MovieVideo.getYoutubeVideoPath(gameVideos[1].key), true, true)

    Log.i("AAAAAAA", "VideoPlayer: ${MovieVideo.getYoutubeVideoPath(gameVideos[1].key)}")
//    }

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