package com.example.testCompose.presentation.ui.compose.movies

import android.annotation.SuppressLint
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import at.huber.youtubeExtractor.YtFile
import coil.compose.rememberImagePainter
import com.example.testCompose.common.NetworkImage
import com.example.testCompose.data.db.remote.service.MovieVideo
import com.example.testCompose.domain.entity.video.Video
import com.example.testCompose.domain.entity.video.VideoList
import com.example.testCompose.presentation.ui.compose.MainDestinations
import com.example.testCompose.presentation.viewModel.MovieVideoViewModel
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import testCompose.R

@ExperimentalAnimationApi
@Composable
fun VideoFilms(
    movieId: Int) {

    val movieVideoTestViewModel = hiltViewModel<MovieVideoViewModel>()
    val getTestVideo = movieVideoTestViewModel.movieVideo.value

    LaunchedEffect(true) {
        movieVideoTestViewModel.getTestVideos(movieId = movieId)
    }

    if (getTestVideo != null) {
        ShowGameVideos(getTestVideo)
    }

}

@ExperimentalAnimationApi
@Composable
fun ShowGameVideos(gameVideos: VideoList) {
    val playingIndex = remember {
        mutableStateOf(0)
    }

    fun onTrailerChange(index: Int) {
        playingIndex.value = index
    }

    Column {
        ListOfVideos(
            modifier = Modifier.weight(1f, fill = true),
            videos = gameVideos.results,
            playingIndex = playingIndex,
            onTrailerChange = { newIndex -> onTrailerChange(newIndex) }
        )
        LazyColumn(
            modifier = Modifier.weight(1f, fill = true),
            content = {
                itemsIndexed(gameVideos.results) { index, trailer ->
                    ShowTrailerss(
                        index = index,
                        trailer = trailer,
                        playingIndex = playingIndex,
                        onTrailerClicked = { newIndex -> onTrailerChange(newIndex) })
                }
            })
    }
}

@Composable
fun ShowTrailerss(
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
        Image(
            contentScale = ContentScale.Crop,
            painter = rememberImagePainter(data = trailer.published_at),
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier
                .height(120.dp)
                .width(120.dp)
                .clip(RoundedCornerShape(20.dp))
                .shadow(elevation = 20.dp)
                .constrainAs(thumbnail) {
                    top.linkTo(parent.top, margin = 8.dp)
                    start.linkTo(parent.start, margin = 8.dp)
                    bottom.linkTo(parent.bottom)
                }
        )
        if (currentlyPlaying.value) {
            Image(
                contentScale = ContentScale.Crop,
                colorFilter = if (trailer.published_at.isEmpty()) ColorFilter.tint(White) else ColorFilter.tint(
                    Green
                ),
                painter = painterResource(id = R.drawable.ic_videoplayer),
                contentDescription = stringResource(id = R.string.BASE_URL),
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .graphicsLayer {
                        clip = true
                        shadowElevation = 20.dp.toPx()
                    }
                    .constrainAs(play) {
                        top.linkTo(thumbnail.top)
                        start.linkTo(thumbnail.start)
                        end.linkTo(thumbnail.end)
                        bottom.linkTo(thumbnail.bottom)
                    }
            )
        }
        Text(
            text = trailer.name,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(thumbnail.top, margin = 8.dp)
                    start.linkTo(thumbnail.end, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    width = Dimension.preferredWrapContent
                    height = Dimension.wrapContent
                },
            color = Black,
            textAlign = TextAlign.Center,
            softWrap = true
        )
        if (currentlyPlaying.value) {
            Text(
                text = stringResource(id = R.string.videos_now_playing),
                color = White,
                textAlign = TextAlign.Center,
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
        TrailerDividers()
    }
}

@Composable
fun TrailerDividers() {
    Divider(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .testTag("Divider"),
        color = Gray
    )
}

@ExperimentalAnimationApi
@Composable
private fun ListOfVideos(videos: List<Video>, modifier: Modifier, playingIndex: State<Int>, onTrailerChange: (Int) -> Unit) {

    Box() {
        videos.forEach { item ->
            VideoPlayerr(gameVideos = item, playingIndex = playingIndex, onTrailerChange = onTrailerChange, modifier = Modifier.height(200.dp).fillMaxWidth())

        }
    }
}

@ExperimentalAnimationApi
@Composable
fun VideoPlayerr(
    gameVideos: Video,
    playingIndex: State<Int>,
    onTrailerChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val visible = remember {
        mutableStateOf(true)
    }
    val videoTitle = remember {
        mutableStateOf(gameVideos.name)
    }

//    val videoTitle = remember {
//        mutableStateOf(gameVideos[playingIndex.value].name)
//    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            this.prepare()
            this.addListener(object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    super.onEvents(player, events)
                    if (player.contentPosition >= 200) visible.value = false
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    onTrailerChange(this@apply.currentPeriodIndex)
                    visible.value = true
                    videoTitle.value = mediaItem?.mediaMetadata?.displayTitle.toString()
                }
            })
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
        }.extract(MovieVideo.getYoutubeVideoPath(gameVideos.key), true, true)

        Log.i("AAAAAAA", "VideoPlayer: ${MovieVideo.getYoutubeVideoPath(gameVideos.key)}")
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

    exoPlayer.seekTo(playingIndex.value, C.TIME_UNSET)
    exoPlayer.playWhenReady = true


    ConstraintLayout(modifier = modifier.background(Black)) {
        val (title, videoPlayer) = createRefs()
        AnimatedVisibility(
            visible = visible.value,
            enter = fadeIn(initialAlpha = 0.4f),
            exit = fadeOut(animationSpec = tween(durationMillis = 250)),
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Text(
                text = videoTitle.value,
                color = White,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
        DisposableEffect(
            AndroidView(
                modifier = modifier
                    .testTag("VideoPlayer")
                    .constrainAs(videoPlayer) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                factory = {
                    PlayerView(context).apply {
                        player = exoPlayer
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                })
        ) {
            onDispose {
                exoPlayer.release()
            }
        }
    }
}