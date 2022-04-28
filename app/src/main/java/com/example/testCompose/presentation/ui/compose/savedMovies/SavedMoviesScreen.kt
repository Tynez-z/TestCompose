package com.example.testCompose.presentation.ui.compose.savedMovies

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testCompose.common.applyGradient
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.movies
import com.example.testCompose.presentation.ui.compose.components.Gauge
import com.example.testCompose.presentation.ui.compose.components.NetworkImage
import com.example.testCompose.presentation.ui.compose.destinations.ArticleScreenDestination
import com.example.testCompose.presentation.viewModel.SavedMoviesViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.FlowPreview
import testCompose.BuildConfig
import testCompose.R
import kotlin.math.absoluteValue

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Destination
@ExperimentalPagerApi
@ExperimentalFoundationApi
@FlowPreview
@ExperimentalComposeUiApi
@Composable
fun SavedMoviesScreen(destinationsNavigator: DestinationsNavigator) {

    val savedMoviesViewModel = hiltViewModel<SavedMoviesViewModel>()
    val uiState = savedMoviesViewModel.uiState.collectAsState()
    val items = uiState.value.movieList ?: emptyList()

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        ListOnPlayingMovies(items, destinationsNavigator)
    }
}

@FlowPreview
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalPagerApi
@Composable
fun ListOnPlayingMovies(movies: List<MovieDetails>, destinationsNavigator: DestinationsNavigator) {
    if (movies.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            HorizontalPager(
                count = movies.size,
                contentPadding = PaddingValues(horizontal = 55.dp),
                state = rememberPagerState(),
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                CardMovieOnPLaying(pageOffset = pageOffset, movie = movies[page]) { id ->
                    movies.find { movieDetails ->
                        movieDetails.id == id
                    }
                    destinationsNavigator.navigate(ArticleScreenDestination(movieId = id))
                }
            }
        }
    }
}

@Composable
fun CardMovieOnPLaying(pageOffset: Float, movie: MovieDetails, onClick: (Int) -> Unit) {
    Column(
        modifier = Modifier
//            .padding(8.dp)
            .graphicsLayer {
                lerp(
                    start = 0.85f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                ).also { scale ->
                    scaleX = scale
                    scaleY = scale
                }
                alpha = lerp(
                    start = 0.5f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )
            }
            .fillMaxWidth()
    ) {
        CardMovie(movie = movie, onClick = onClick)
    }
}

@Composable
fun CardMovie(movie: MovieDetails, onClick: (Int) -> Unit) {
    Column(
        modifier = Modifier
    ) {
        Card(
            modifier = Modifier
                .clickable {
                    onClick(movie.id)
                }
                .height(493.dp)
                .width(296.dp)
                .border(BorderStroke(2.dp, Color.DarkGray), RoundedCornerShape(20.dp)),
            ) {
            Column(
                modifier = Modifier
                    .background(Color.Black)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                NetworkImage(
                    networkUrl = BuildConfig.BASE_POSTER_PATH + movie.poster_path,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .applyGradient()
                        .height(329.dp)
                )
                MovieInfoForCard(movie = movie)
            }
        }
    }
}

@Composable
fun MovieInfoForCard(movie: MovieDetails) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(end = 20.dp, bottom = 15.dp)
        .verticalScroll(rememberScrollState())) {
        Column(modifier = Modifier) {
            Text(
                text = movie.title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 18.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(start = 18.dp)
                        .size(15.dp)
                )
                Text(
                    text = movie.release_date.substring(0, 4),
                    color = colorResource(id = R.color.tv_pager_info),
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rate),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(start = 18.dp)
                        .size(15.dp)
                )
                Text(
                    text = movie.vote_average.toString(),
                    color = colorResource(id = R.color.tv_pager_info),
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.voterate),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(start = 18.dp)
                        .size(15.dp)
                )
                Text(
                    text = movie.vote_count.toString(),
                    color = colorResource(id = R.color.tv_pager_info),
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        }
        Gauge(
            Modifier
                .size(60.dp)
                .align(Alignment.CenterEnd)
                .clip(CircleShape),
            value = ((movie.vote_average ?: 0.0) * 0.1).toFloat(),
            bgColor = Color.Black,
            strokeSize = 8f,
            fontSize = 12.sp,
            animated = true
        )
    }
}

@ExperimentalMaterialApi
@Preview("MovieSavedCard")
@Composable
fun MovieSavedCardPreview() {
    CardMovie(
        movie = movies.last(),
        onClick = {})
}