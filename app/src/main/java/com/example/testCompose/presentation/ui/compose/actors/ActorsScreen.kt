package com.example.testCompose.presentation.ui.compose.actors

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.testCompose.BuildConfig
import com.example.testCompose.R
import com.example.testCompose.common.PagingErrorMessage
import com.example.testCompose.common.PagingLoadItem
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.entity.actors.Actors
import com.example.testCompose.presentation.ui.compose.components.NetworkImage
import com.example.testCompose.presentation.ui.compose.movies.MovieItem
import com.example.testCompose.presentation.viewModel.MovieDetailViewModel
import com.example.testCompose.presentation.viewModel.SavedMoviesViewModel
import kotlinx.coroutines.launch
import kotlin.collections.mutableListOf

@Composable
fun ActorsScreen(
    navController: NavController,
    viewModel: ActorsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var count by remember { mutableIntStateOf(0) }
//    val list = remember { mutableListOf("a") }
    var list by remember { mutableStateOf(listOf("a")) }
    Test(
        list = list,
        count = count,
        clickOnCount = { count++
            list = list + "b"
//            list.add("b")
        }
    )

//    Column(modifier = Modifier.fillMaxSize()) {
//        ActorsContent(
//            actors = uiState.actorsList,
//            onActorClick = {
//
//            })
//    }
}

@Composable
fun Test(list: List<String>,count: Int, clickOnCount: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    )
    {
        Header()
        TestList(list = list)
        Button(
            content = { Text(text = "Clicked:$count", color = Color.Black) },
            onClick = { clickOnCount() })
    }
}

@Composable
fun TestList(
    list: List<String>
){
    LazyColumn() {
        items(list.size) { index ->
            list[index].let { item ->
                Text(text = item, color = Color.White, fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun Header() {
    Text(text = "Header", color = Color.Blue)
}

@Composable
fun ActorsContent(
    actors: List<Actors>,
    onActorClick: (Actors) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(3.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = actors,
            key = { it.id }
        ) { actor ->
            ActorItem(
                actor = actor,
                onItemClick = onActorClick
            )
        }
    }
}


@Composable
fun ActorItem(actor: Actors, onItemClick: (Actors) -> Unit) {
    Surface(
        color = Color.Transparent,
        contentColor = colors.onBackground
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .clickable
                {
                    onItemClick.invoke(actor)
                }, verticalArrangement = Arrangement.Center
        ) {
            ActorImage(actor = actor)
            Text(
                text = actor.name,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun ActorImage(
    actor: Actors
) {
    NetworkImage(
        networkUrl = BuildConfig.BASE_POSTER_PATH + actor.profile_path,
        modifier = Modifier
            .padding(10.dp)
            .size(height = 120.dp, width = 70.dp)
            .clip(RoundedCornerShape(18.dp)),
        contentScale = ContentScale.Crop,
        placeholder = ImageBitmap.imageResource(R.drawable.film),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360)
@Composable
fun ActorsContentPreview() {
    val sampleActors = listOf(
        Actors(
            id = 1,
            name = "Ryan Gosling",
            character = "Officer K",
            popularity = 88.4f,
            profile_path = null
        ),
        Actors(
            id = 2,
            name = "Ana de Armas",
            character = "Joi",
            popularity = 72.1f,
            profile_path = null
        ),
        Actors(
            id = 3,
            name = "Harrison Ford",
            character = "Deckard",
            popularity = 65.0f,
            profile_path = null
        ),
        Actors(
            id = 4,
            name = "Jared Leto",
            character = "Niander Wallace",
            popularity = 54.3f,
            profile_path = null
        ),
        Actors(
            id = 5,
            name = "Robin Wright",
            character = "Lt. Joshi",
            popularity = 43.2f,
            profile_path = null
        ),
        Actors(
            id = 6,
            name = "Dave Bautista",
            character = "Sapper Morton",
            popularity = 39.5f,
            profile_path = null
        ),
        Actors(
            id = 7,
            name = "Sylvia Hoeks",
            character = "Luv",
            popularity = 21.7f,
            profile_path = null
        ),
        Actors(
            id = 8,
            name = "Mackenzie Davis",
            character = "Mariette",
            popularity = 18.9f,
            profile_path = null
        ),
        Actors(
            id = 9,
            name = "Carla Juri",
            character = "Ana Stelline",
            popularity = 14.2f,
            profile_path = null
        )
    )

    ActorsContent(
        actors = sampleActors,
        onActorClick = {}
    )
}