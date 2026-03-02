package com.example.testCompose.presentation.ui.compose.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testCompose.domain.entity.genres.GenreItem
import com.example.testCompose.domain.entity.genresFake
import com.example.testCompose.presentation.ui.compose.movies.DrawRect
import com.example.testCompose.presentation.viewModel.MoviesViewModel
import kotlinx.coroutines.FlowPreview

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
