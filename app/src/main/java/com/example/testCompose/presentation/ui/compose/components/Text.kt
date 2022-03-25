package com.example.testCompose.presentation.ui.compose.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit

typealias textStyle = androidx.compose.ui.text.TextStyle

@Composable
fun MovieTitleText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.White,
    style: textStyle = MaterialTheme.typography.h2,
    textAlign: TextAlign = TextAlign.Center,
    fontWeight: FontWeight? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        style = style,
        textAlign = textAlign,
        fontWeight = fontWeight,
        fontSize = fontSize
    )
}