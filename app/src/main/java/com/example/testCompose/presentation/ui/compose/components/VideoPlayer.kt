package com.example.testCompose.presentation.ui.compose.components

import android.net.Uri
import android.util.Log
import android.util.SparseArray
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YtFile
import com.example.testCompose.data.db.remote.service.MovieVideo
import com.example.testCompose.domain.entity.video.Video
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import org.json.JSONException

@ExperimentalAnimationApi
@Composable
fun VideoPlayer(
    movieVideo: List<Video>,
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
                    Toast.makeText(context, "This video is unavailable", Toast.LENGTH_SHORT).show()
                }
            }
        }.extract(MovieVideo.getYoutubeVideoPath(movieVideo[0].key), true, true)
    } catch (e: Exception) {
        when (e) {
            is IndexOutOfBoundsException, is JSONException -> {
                OutlinedButton(
                    onClick = {},
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.DarkGray.copy(
                            alpha = 0.65f
                        )
                    ),
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
        }
    }

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