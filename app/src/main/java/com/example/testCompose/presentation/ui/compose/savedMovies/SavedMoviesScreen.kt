package com.example.testCompose.presentation.ui.compose.savedMovies

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.testCompose.common.*
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.presentation.ui.compose.MainDestinations
import com.example.testCompose.presentation.viewModel.SearchMovieViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import testCompose.BuildConfig
import testCompose.R

@ExperimentalFoundationApi
@FlowPreview
@ExperimentalComposeUiApi
@Composable
fun SavedMoviesScreen(navController: NavController, scaffoldState: ScaffoldState) {

    val searchViewModel = hiltViewModel<SearchMovieViewModel>()

    val keyboardController = LocalSoftwareKeyboardController.current
    var query by remember {
        mutableStateOf(TextFieldValue(searchViewModel.searchQuery.value))
    }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

        TextField(value = query, onValueChange = { value ->
            query = value
            searchViewModel.updateQuery(value.text)
        })

        searchViewModel.searchResults?.let {
            SearchMoviesResults(
                searchTerm = query.text,
                searchResults = it,
                navController = navController
            )
        }


    }

}

@Composable
fun TextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
//            .height(65.dp)
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
            Text(text = "Search movies")
        },
        maxLines = 1,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            disabledTextColor = Color.DarkGray,
            placeholderColor = Color.DarkGray,
            disabledPlaceholderColor = Color.DarkGray,

            backgroundColor = Color.Black,
            cursorColor = Color.Black,
            errorCursorColor = Color.Red,
            focusedIndicatorColor = Color.Green,
            unfocusedIndicatorColor = Color.White,

//            unfocusedIndicatorColor = Color(0xFFFFD040),
            errorIndicatorColor = Color.Red
        )
    )
}

@ExperimentalFoundationApi
@Composable
fun SearchMoviesResults(
    searchTerm: String,
    searchResults: Flow<PagingData<MovieDetails>>,
    navController: NavController
) {
    val lazyPagingItems = searchResults.collectAsLazyPagingItems()

    LazyVerticalGrid(cells = GridCells.Fixed(3), modifier = Modifier.fillMaxSize().padding(bottom = 60.dp)) {
        items(lazyPagingItems.itemCount) { item ->
            lazyPagingItems[item]?.let {
                MovieItemSearch(searchMovie = it, onItemClick = {
                    navController.navigate(MainDestinations.ArticleMoviesRoute.destination + "/${it.id}")

                    Log.i("AAA", "SearchMoviesResults:${it.id} ")
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
            modifier = Modifier.height(180.dp).width(120.dp), contentScale = ContentScale.Crop
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