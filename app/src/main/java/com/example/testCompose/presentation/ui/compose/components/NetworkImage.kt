package com.example.testCompose.presentation.ui.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
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
    shimmerParams: ShimmerParams? = null,
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
}

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



