package com.example.testCompose.presentation.ui.compose.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.movies
import kotlinx.coroutines.launch
import testCompose.BuildConfig
import testCompose.R

@ExperimentalMaterialApi
@SuppressLint("UnrememberedMutableState")
@Composable
fun BottomSheetLayout(
    selectedMovie: MovieDetails?,
    onMovieClick: (Int) -> Unit,
    bottomSheetState: ModalBottomSheetState
) {
    val coroutineScope = rememberCoroutineScope()

    if (selectedMovie != null) {
        SpecificSurface(
            shape = RoundedCornerShape(topStartPercent = 5, topEndPercent = 5),
            modifier = Modifier
                .wrapContentWidth()
                .height(350.dp)
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
                                        color = colorResource(id = R.color.tv_info_bottomsheet),
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row {
                                        Gauge(
                                            Modifier
                                                .size(60.dp)
                                                .clip(CircleShape),
                                            value = ((selectedMovie.vote_average ?: 0.0) * 0.1).toFloat(),
                                            bgColor = Color.Black,
                                            strokeSize = 8f,
                                            fontSize = 12.sp,
                                            animated = true
                                        )
                                        Column(
                                            modifier = Modifier.padding(start = 8.dp),
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            if (selectedMovie.release_date.isEmpty()) {

                                            } else {

                                            }
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.calendar),
                                                    contentDescription = "",
                                                    Modifier.size(18.dp)
                                                )
                                                Text(
                                                    text = if (selectedMovie.release_date.isEmpty()) {
                                                        0.toString()
                                                    } else {
                                                        selectedMovie.release_date
                                                    },
                                                    color = colorResource(id = R.color.tv_info_bottomsheet),
                                                    fontSize = 12.sp,
                                                    maxLines = 1
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.rate),
                                                    contentDescription = "",
                                                    Modifier.size(18.dp)
                                                )
                                                Text(
                                                    text = selectedMovie.vote_average.toString(),
                                                    color = colorResource(id = R.color.tv_info_bottomsheet),
                                                    fontSize = 12.sp,
                                                    maxLines = 1
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.voterate),
                                                    contentDescription = "",
                                                    Modifier.size(18.dp)
                                                )
                                                Text(
                                                    text = selectedMovie.vote_count.toString(),
                                                    color = colorResource(id = R.color.tv_info_bottomsheet),
                                                    fontSize = 12.sp,
                                                    maxLines = 1
                                                )
                                            }
                                        }
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
                                color = colorResource(id = R.color.tv_info_bottomsheet),
                                maxLines = 4,
                                lineHeight = 18.sp,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
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
}

@Composable
fun SpecificSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    contentColor: Color = MaterialTheme.colors.primary,
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
                .clip(RoundedCornerShape(6.dp))
                .clickable {
                    onMovieSelected(movie.id)
                },
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
            color = colorResource(id = R.color.tv_info_bottomsheet),
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

@ExperimentalMaterialApi
@Preview("Bottom Sheet Content")
@Composable
fun BottomSheetPreview() {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    BottomSheetLayout(
        selectedMovie = movies.last(),
        onMovieClick = {},
        bottomSheetState = sheetState)
}