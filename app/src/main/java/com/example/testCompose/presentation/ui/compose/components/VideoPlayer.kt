package com.example.testCompose.presentation.ui.compose.components

import android.content.Context
import android.util.Log
import android.util.SparseArray
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.testCompose.common.EMPTY_STRING
import com.example.testCompose.domain.entity.video.Video

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun VideoPlayer(
    movieVideo: List<Video>,
    playingIndex: State<Int>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var playerState by remember { mutableStateOf<PlayerState>(PlayerState.Loading) }
    var videoUrl by remember { mutableStateOf<String?>(null) }

    // Extract YouTube URL
    LaunchedEffect(movieVideo, playingIndex.value) {
        if (movieVideo.isEmpty()) {
            playerState = PlayerState.Error("No videos available")
            return@LaunchedEffect
        }

        val index = playingIndex.value.coerceIn(0, movieVideo.lastIndex)
        playerState = PlayerState.Loading

        extractYouTubeUrl(
            context = context,
            videoKey = movieVideo[index].key ?: EMPTY_STRING,
            onSuccess = { url ->
                videoUrl = url
                playerState = PlayerState.Ready
            },
            onError = { error ->
                playerState = PlayerState.Error(error)
            }
        )
    }

    when (val state = playerState) {
        is PlayerState.Loading -> {
            LoadingIndicator(modifier = modifier)
        }
        is PlayerState.Error -> {
            ErrorMessage(
                message = state.message,
                modifier = modifier
            )
        }
        is PlayerState.Ready -> {
            videoUrl?.let { url ->
                Media3Player(
                    videoUrl = url,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
private fun Media3Player(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                    .build(),
                true // handleAudioFocus
            )
            .setHandleAudioBecomingNoisy(true)
            .build()
    }

    // Set media source when URL changes
    LaunchedEffect(videoUrl) {
        val mediaItem = MediaItem.fromUri(videoUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    // Lifecycle management
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> exoPlayer.pause()
                Lifecycle.Event.ON_RESUME -> exoPlayer.play()
                Lifecycle.Event.ON_DESTROY -> {
                    exoPlayer.stop()
                    exoPlayer.release()
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = true
                    setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
private fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        OutlinedButton(
            onClick = {},
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.DarkGray.copy(alpha = 0.65f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = message,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

private sealed class PlayerState {
    data object Loading : PlayerState()
    data object Ready : PlayerState()
    data class Error(val message: String) : PlayerState()
}

private const val TAG = "YouTubeExtractor"
private const val ITAG_360P = 18
private const val ITAG_720P = 22

fun extractYouTubeUrl(
    context: Context,
    videoKey: String,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    val youtubeUrl = "https://www.youtube.com/watch?v=$videoKey"

    try {
        object : YouTubeExtractor(context) {
            override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                videoMeta: VideoMeta?
            ) {
                if (ytFiles == null) {
                    onError("This video is unavailable")
                    return
                }

                // Try 720p first, fall back to 360p
                val ytFile = ytFiles.get(ITAG_720P) ?: ytFiles.get(ITAG_360P)
                val downloadUrl = ytFile?.url

                if (downloadUrl != null) {
                    onSuccess(downloadUrl)
                } else {
                    onError("This video is unavailable")
                }
            }
        }.extract(youtubeUrl)
    } catch (e: Exception) {
        Log.e(TAG, "YouTube extraction failed", e)
        onError("This video is unavailable")
    }
}