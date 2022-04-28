package com.example.testCompose.presentation.ui.compose.movies

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.testCompose.common.*
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.presentation.ui.compose.MainDestinations
import com.example.testCompose.presentation.ui.compose.components.FilterMoviesMenu
import com.example.testCompose.presentation.ui.compose.components.NetworkImage
import com.google.android.material.shape.Shapeable
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import testCompose.BuildConfig
import testCompose.R

@Composable
fun Search(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier
) {
    Row(Modifier) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.Black),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.White
                )
            },
            trailingIcon = {
                AnimatedVisibility(
                    visible = value.text.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(onClick = { onValueChange(TextFieldValue()) }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear text",
                            tint = Color.White
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(text = "Search...", fontSize = 14.sp)
            },
            maxLines = 1,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                disabledTextColor = Color.DarkGray,
                placeholderColor = Color.DarkGray,
                disabledPlaceholderColor = Color.DarkGray,
                backgroundColor = Color.Black,
                cursorColor = Color.White,
                errorCursorColor = Color.Red,
                focusedIndicatorColor = Color.Green,
                unfocusedIndicatorColor = Color.White,
                errorIndicatorColor = Color.Red
            ),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 16.dp,
                bottomStart = 0.dp,
                bottomEnd = 16.dp
            )
        )
    }
}

@ExperimentalFoundationApi
@FlowPreview
@Composable
fun OnClickSettings(onSettingsClick: () -> Unit, onFilterChanged: (Int) -> Unit) {
    FilterMoviesMenu(
        onFilterChanged = onFilterChanged,
        onSettingsClick = onSettingsClick
    )
}

@Composable
fun OnClickGenres() {
    Row(
        modifier = Modifier
            .border(
                BorderStroke(1.dp, Color.White),
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 0.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 0.dp
                )
            )
            .height(48.dp)
            .wrapContentSize()
    ) {
        Text(
            text = "Genre",
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Image(
            painterResource(id = R.drawable.downgenres),
            contentDescription = "",
            modifier = Modifier
                .size(12.dp)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(12.dp))
    }
}


@ExperimentalFoundationApi
@Composable
fun SearchMoviesResults(
    searchTerm: String,
    searchResults: Flow<PagingData<MovieDetails>>?,
    navController: NavController
) {
    if (searchResults != null) {
        val lazyPagingItems = searchResults.collectAsLazyPagingItems()

        LazyVerticalGrid(cells = GridCells.Fixed(3), modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp)) {
            items(lazyPagingItems.itemCount) { item ->
                lazyPagingItems[item]?.let {
                    MovieItemSearch(searchMovie = it, onItemClick = { movie ->
                        navController.navigate(MainDestinations.ArticleMoviesRoute.destination + "/${movie.id}")
                    })
                }
            }

            lazyPagingItems.apply {
                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        item {
                            PagingLoadingView(modifier = Modifier.fillMaxSize())
                        }
                    }
                    is LoadState.Error -> {
                        val state = lazyPagingItems.loadState.refresh as LoadState.Error
                        item {
                            PagingErrorMessage(
                                message = state.error.localizedMessage
                                    ?: "There was an error. Try again!"
                            )
                        }
                    }
                    LoadState.NotLoading(true) -> {
                        if (lazyPagingItems.itemCount == 0) {
                            item {
                                SearchResultEmptyMessage(searchTerm = searchTerm)
                            }
                        }
                    }
                }

                when (loadState.append) {
                    is LoadState.Loading -> {
                        item {
                            PagingLoadItem()
                        }
                    }
                    is LoadState.Error -> {
                        val state = lazyPagingItems.loadState.append as LoadState.Error
                        item {
                            PagingErrorItem(
                                message = state.error.localizedMessage
                                    ?: stringResource(id = R.string.error_try_again)
                            )
                        }
                    }

                    LoadState.NotLoading(true) -> {
                        if (lazyPagingItems.itemCount == 0) {
                            item {
                                SearchResultEmptyMessage(searchTerm = searchTerm)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItemSearch(searchMovie: MovieDetails, onItemClick: (MovieDetails) -> Unit) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .padding(10.dp)
            .fillMaxWidth()
            .clickable {
                onItemClick.invoke(searchMovie)
            }, horizontalAlignment = Alignment.CenterHorizontally

    ) {
        NetworkImage(
            networkUrl = BuildConfig.BASE_POSTER_PATH + searchMovie.poster_path,
            placeholder = ImageBitmap.imageResource(R.drawable.placeholdericon),
            modifier = Modifier.clip(RoundedCornerShape(10.dp)).width(156.dp)
                .height(210.dp),
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
fun DrawRect() {
    Column(modifier = Modifier
        .wrapContentSize()
        .drawWithContent {
            drawContent()
            clipRect {
                val strokeWidth = 6f
                val y = size.height

                drawLine(
                    brush = SolidColor(Color.White),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Square,
                    start = Offset(x = 0*density, y = 0*density),
                    end = Offset(x = size.width, y = 0*density)
                )

                drawLine(
                    brush = SolidColor(Color.White),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Square,
                    start = Offset.Zero.copy(y = 0*density),
                    end = Offset.Zero.copy(y = size.height)
                )

                drawLine(
                    brush = SolidColor(Color.White),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Square,
                    start = Offset.Zero.copy(y = y),
                    end = Offset(x = size.width, y = y)
                )

                drawLine(
                    brush = SolidColor(Color.White),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Square,
                    start = Offset(x = size.width, y = y),
                    end = Offset(x = size.width, y = y)
                )
            }
        }
    ) {
        Row(modifier = Modifier.padding(start = 0.dp).height(48.dp)) {
            Text(
                text = "Genre",
                fontSize = 15.sp,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Image(
                painterResource(id = R.drawable.downgenres),
                contentDescription = "",
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}


@Preview("DrawRect")
@Composable
fun PreviewDrawRect() {
    DrawRect()
}

@Preview("Search")
@Composable
fun PreviewTextField() {
    Search(value = TextFieldValue(), onValueChange = {}, modifier = Modifier)
}

@Preview("Select Genres")
@Composable
fun PreviewGenres() {
    OnClickGenres()
}