package com.example.testCompose.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

fun Job.stop() {
    apply {
        cancelChildren()
        cancel()
    }
}

@Composable
fun PagingLoadingView(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            color = Color.Red, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun PagingLoadItem() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            color = Color.Red,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun PagingErrorMessage(modifier: Modifier = Modifier, message: String) {
    Column(
        modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = message,
            style = MaterialTheme.typography.subtitle2,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.error
        )

        Spacer(modifier = modifier.height(16.dp))
    }
}

@Composable
fun PagingErrorItem(message: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            maxLines = 1,
            modifier = modifier.weight(1f),
            style = MaterialTheme.typography.subtitle2, color = MaterialTheme.colors.error
        )

    }
}

@Composable
fun SearchResultEmptyMessage(searchTerm: String) {
    Text(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
        textAlign = TextAlign.Center,
        color = Color.White,
        text = buildAnnotatedString {
            append("No results for ")
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.primary
                )
            ) {
                append("$searchTerm")
            }
            append(".")
            append("Try searching for something different.")
        })
}

fun Long.fromMinutesToHHmm(minutes: Long): String {
    val hour: Long = TimeUnit.HOURS.toHours(minutes) / 60
    val minute: Long = TimeUnit.MINUTES.toMinutes(minutes) % 60
    return String.format("%dh : %02dm", hour, minute)
}

@SuppressLint("SimpleDateFormat")
fun String.convertDate(date: String): String {
    //ISO_INSTANT
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
    return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(simpleDateFormat.parse(date)!!)
}

@Composable
fun Dialog(
    showDialog: Boolean,
    title: String?,
    text: String,
    confirmText: String,
    onChangeNotificationStateClicked: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onChangeNotificationStateClicked()
            },
            title = if (title is String) {
                {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }
            } else null,
            text = {
                Text(text, color = Color.Black)
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(onClick = {
                        onChangeNotificationStateClicked()
                    }) {
                        Text(confirmText, color = Color.Black)
                    }
                }
            },
            shape = RoundedCornerShape(19.dp)
        )
    }
}

@Composable
fun CircularProgressBar(progressBarVisibility: Boolean) {

    if (progressBarVisibility) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primaryVariant
            )
        }
    }
}

@Composable
fun LoadingItem() {
    CircularProgressIndicator(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentWidth(
                Alignment.CenterHorizontally
            )
    )
}

fun Modifier.applyGradient(): Modifier {
    return drawWithCache {
        val gradient = Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.Black),
            startY = size.height / 3,
            endY = size.height
        )
        onDrawWithContent {
            drawContent()
            drawRect(gradient, blendMode = BlendMode.Multiply)
        }
    }
}

