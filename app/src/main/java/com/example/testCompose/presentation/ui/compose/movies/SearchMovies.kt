package com.example.testCompose.presentation.ui.compose.movies

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.testCompose.common.*
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.presentation.ui.compose.MainDestinations
import com.example.testCompose.presentation.ui.compose.components.NetworkImage
import kotlinx.coroutines.flow.Flow
import testCompose.BuildConfig
import testCompose.R

@Composable
fun TextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier,
    showSettingsDialog: MutableState<Boolean>,
) {
    Row(
        Modifier
            .padding(12.dp)
//        .padding(top = 16.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        OutlinedTextField(
            modifier = Modifier
//                .fillMaxWidth()
                .weight(1f)
                .padding(end = 16.dp)
//                .padding(16.dp)
            .height(47.dp)
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
            )
        )
        IconButton(onClick = { showSettingsDialog.value = true }) {
            Icon(
                Icons.Default.Menu,
                contentDescription = stringResource(id = R.string.settings),
                tint = Color.White,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
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

        Log.i("AAAA", "SearchMoviesResults: ${searchTerm}")
    }

//    else {
//        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
//            Text(
//                text = "Type to find a film",
//                color = Color.Gray,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//                    .wrapContentWidth(Alignment.CenterHorizontally)
//            )
//        }
//    }
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
            modifier = Modifier
                .height(180.dp)
                .width(120.dp), contentScale = ContentScale.Crop
        )
        Text(
            text = searchMovie.title,
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            overflow = TextOverflow.Ellipsis
        )
    }
}