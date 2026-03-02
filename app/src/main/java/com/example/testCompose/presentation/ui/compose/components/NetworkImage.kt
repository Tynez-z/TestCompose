package com.example.testCompose.presentation.ui.compose.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.toSize
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.testCompose.R


@Composable
fun NetworkImage(
    networkUrl: Any?,
    modifier: Modifier = Modifier,
    placeholder: ImageBitmap? = null,
    contentScale: ContentScale = ContentScale.FillBounds,
    colorFilter: ColorFilter? = null
) {
    val context = LocalContext.current
    val model = networkUrl ?: R.drawable.nophoto

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(context)
            .data(model)
            .crossfade(true)
            .crossfade(300)
            .build(),
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale,
        alignment = Alignment.Center,
        colorFilter = colorFilter
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                if (placeholder != null) {
                    Image(
                        bitmap = placeholder,
                        contentDescription = null,
                        contentScale = contentScale,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .shimmerEffect()
                    )
                }
            }

            is AsyncImagePainter.State.Error -> {
                Image(
                    painter = painterResource(R.drawable.nophoto),
                    contentDescription = null,
                    contentScale = contentScale,
                    modifier = Modifier.matchParentSize()
                )
            }

            else -> {
                // Success or Empty - show the loaded image
                SubcomposeAsyncImageContent()
            }
        }
    }
}

/**
 * Native Compose Shimmer Effect
 * Creates a shimmering gray brush background.
 */
fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
    val transition = rememberInfiniteTransition(label = "Shimmer")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width,
        targetValue = 2 * size.width,
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ),
        label = "ShimmerOffset"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size.toSize()
        }
}

//    @Composable
//    fun NetworkImage(
//        networkUrl: Any?,
//        modifier: Modifier = Modifier,
//        circularReveal: CircularReveal? = null,
//        placeholder: ImageBitmap? = null,
//        contentScale: ContentScale = ContentScale.FillBounds,
//        bitmapPalette: BitmapPalette? = null,
//        shimmerParams: ShimmerParams? = null,
//    ) {
//        if (networkUrl == null) {
//            CoilImage(
//                imageModel = networkUrl,
//                circularReveal = circularReveal,
//                placeHolder = placeholder,
//                modifier = modifier,
//                contentScale = contentScale,
//                bitmapPalette = bitmapPalette,
//                error = ImageBitmap.imageResource(
//                    R.drawable.nophoto
//                )
//            )
//        } else {
//            CoilImage(
//                imageModel = networkUrl,
//                circularReveal = circularReveal,
//                placeHolder = placeholder,
//                modifier = modifier,
//                contentScale = contentScale,
//                bitmapPalette = bitmapPalette,
//                error = ImageBitmap.imageResource(
//                    R.drawable.nophoto
//                )
//            )
//        }
//    }

//@Composable
//fun NetworkImage(
//    networkUrl: Any?,
//    modifier: Modifier = Modifier,
//    context: Context = LocalContext.current,
//    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
//    alignment: Alignment = Alignment.Center,
//    alpha: Float = DefaultAlpha,
//    contentScale: ContentScale = ContentScale.Crop,
//    circularReveal: CircularReveal? = null,
//    colorFilter: ColorFilter? = null,
//    placeHolder: Any? = null,
//    error: Any? = null,
//    @DrawableRes previewPlaceholder: Int = 0,
//    loading: @Composable ((imageState: CoilImageState.Loading) -> Unit)? = null,
//    success: @Composable ((imageState: CoilImageState.Success) -> Unit)? = null,
//    failure: @Composable ((imageState: CoilImageState.Failure) -> Unit)? = null,
//) {
//    if (networkUrl == null) {
//        CoilImage(
//            imageModel = networkUrl,
//            context = context,
//            lifecycleOwner = lifecycleOwner,
//            modifier = modifier,
//            alignment = alignment,
//            contentScale = contentScale,
//            alpha = alpha,
//            colorFilter = colorFilter,
//            circularReveal = circularReveal,
//            previewPlaceholder = previewPlaceholder,
//            placeHolder = ImageBitmap.imageResource(R.drawable.nophoto),
////            error = ImageBitmap.imageResource(
////                R.drawable.nophoto
////            )
//        )
//    } else {
//        CoilImage(
//            imageModel = networkUrl,
//            context = context,
//            lifecycleOwner = lifecycleOwner,
//            modifier = modifier,
//            alignment = alignment,
//            contentScale = contentScale,
//            alpha = alpha,
//            colorFilter = colorFilter,
//            circularReveal = circularReveal,
//            previewPlaceholder = previewPlaceholder,
//            loading = {
////                placeHolder?.let {
////                    ImageBySource(
////                        source = ImageBitmap.imageResource(R.drawable.nophoto),
////                        modifier = modifier,
////                        alignment = alignment,
////                        contentDescription = contentDescription,
////                        contentScale = ContentScale.Crop,
////                        colorFilter = colorFilter,
////                        alpha = alpha
////                    )
////                }
//                PagingLoadItem()
//            },
//            failure = {
//                placeHolder.let {
//                    ImageBitmap.imageResource(R.drawable.nophoto)
//                }
//
////                error?.let {
////                    ImageBySource(
////                        source = ImageBitmap.imageResource(R.drawable.nophoto),
////                        modifier = modifier,
////                        alignment = alignment,
////                        contentDescription = contentDescription,
////                        contentScale = ContentScale.Crop,
////                        colorFilter = colorFilter,
////                        alpha = alpha,
////                    )
////                }
//                ImageBitmap.imageResource(R.drawable.nophoto)
//            }
//        )
//    }
//}



