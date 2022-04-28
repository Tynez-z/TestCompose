package com.example.testCompose.presentation.ui.compose.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testCompose.domain.entity.genres.GenreItem
import com.example.testCompose.domain.entity.genresFake
import com.example.testCompose.presentation.ui.compose.movies.DrawRect
import com.example.testCompose.presentation.ui.compose.movies.OnClickGenres
import com.example.testCompose.presentation.viewModel.MoviesViewModel
import kotlinx.coroutines.FlowPreview
import testCompose.R

@SuppressLint("FlowOperatorInvokedInComposition")
@ExperimentalFoundationApi
@FlowPreview
@Composable
fun FilterMoviesMenu(
    onFilterChanged: (Int) -> Unit,
    onSettingsClick: () -> Unit
) {

    val moviesViewModel = hiltViewModel<MoviesViewModel>()
    val uiState by moviesViewModel.uiState.collectAsState()
    val item = uiState.genres

    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = {
        expanded = true
        onSettingsClick()
    }) {
//        Icon(
//            Icons.Default.Menu,
//            contentDescription = stringResource(id = R.string.settings),
//            tint = Color.White,
//            modifier = Modifier
//        )
//        OnClickGenres()
        DrawRect()
        }
//        if (showMenu) {
            Log.i("AAAA", "MENU STATE CHANGED")
            MaterialTheme(shapes = Shapes(RoundedCornerShape(6.dp))) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    Modifier
                        .requiredHeightIn(max = 300.dp)
                        .requiredWidth(200.dp)
                        .background(Color.Black)
                ) {
                    item?.forEach { item ->
                        FilterMenuItem(filter = item, onItemClick = {
                            onFilterChanged(item.id)
                        })
                    }
                }
            }
//        }
}


@Composable
fun FilterMenuItem(
    filter: GenreItem,
    onItemClick: () -> Unit,
) {
    DropdownMenuItem(onClick = onItemClick) {
        Text(text = filter.name, color = Color.White)
    }
}

@FlowPreview
@Preview("Filter")
@Composable
fun PreviewFilter() {
    FilterMenuItem(filter = genresFake.last()) {
    }
}
