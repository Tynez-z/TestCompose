package com.example.testCompose.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.widget.Placeholder
import com.example.testCompose.presentation.ui.theme.shimmerHighLightColor
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.palette.BitmapPalette
import testCompose.R

@Composable
fun NetworkImage(
    networkUrl: Any?,
    modifier: Modifier = Modifier,
    circularReveal: CircularReveal? = null,
    placeholder: ImageBitmap? = null,
    contentScale: ContentScale = ContentScale.FillBounds,
    bitmapPalette: BitmapPalette? = null,
    shimmerParams: ShimmerParams? = ShimmerParams(
        baseColor = MaterialTheme.colors.background,
        highlightColor = shimmerHighLightColor,
        dropOff = 0.65f,
        tilt = 20f,
        durationMillis = 350
    ),

    ) {

    if (networkUrl == null) {
        CoilImage(
            imageModel = networkUrl,
            circularReveal = circularReveal,
            placeHolder = placeholder,
            modifier = modifier,
            contentScale = contentScale,
            bitmapPalette = bitmapPalette,
            error = ImageBitmap.imageResource(
                R.drawable.nophoto
            )
        )
    } else {
        CoilImage(
            imageModel = networkUrl,
            circularReveal = circularReveal,
            placeHolder = placeholder,
            modifier = modifier,
            contentScale = contentScale,
            bitmapPalette = bitmapPalette,
            error = ImageBitmap.imageResource(
                R.drawable.nophoto
            )
        )
    }

//    val url = networkUrl ?: return
//    if (shimmerParams == null) {
//        CoilImage(
//            imageModel = url,
//            modifier = modifier,
//            circularReveal = circularReveal,
//            contentScale = contentScale,
//            bitmapPalette = bitmapPalette,
//            loading = {
//                ShimmerParams(
//                    baseColor = MaterialTheme.colors.background,
//                    highlightColor = shimmerHighLightColor,
//                    dropOff = 0.65f,
//                    tilt = 20f,
//                    durationMillis = 350
//                )
//            },
//            failure = {
//                Text(
//                    text = "image request failed.",
//                    textAlign = TextAlign.Center,
//                    style = MaterialTheme.typography.body2,
//                    modifier = Modifier.fillMaxSize()
//                )
//            }
//        )
//    } else {
//        CoilImage(
//            imageModel = url,
//            modifier = modifier,
//            circularReveal = circularReveal,
//            contentScale = contentScale,
//            bitmapPalette = bitmapPalette,
//            loading = {ShimmerParams(
//                baseColor = MaterialTheme.colors.background,
//                highlightColor = shimmerHighLightColor,
//                dropOff = 0.65f,
//                tilt = 20f,
//                durationMillis = 350
//            )},
//
//        )
//    }
}