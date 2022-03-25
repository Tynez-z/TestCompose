package com.example.testCompose.presentation.ui.theme

import android.annotation.SuppressLint
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
//val Teal200 = Color(0xFF03DAC5)
val shimmerHighLightColor = Color(0xA3C2C2C2)

@SuppressLint("ConflictingOnColor")
val LightThemeColors = lightColors(
    primary = Color(0xFFE50914),
    primaryVariant = Color(0xFF971C1C),
    secondary = Color(0xFFE50914),
    secondaryVariant = Color(0xFF831010),
    background = Color.White,
    surface = Color.White,
    error = Color(0xFFE50914),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color(0xFF1C1C1C),
    onError = Color.White
)

val DarkThemeColors = darkColors(
    primary = Color(0xFFE50914),
    primaryVariant = Color(0xFF971C1C),
    secondary = Color(0xFFE50914),
    secondaryVariant = Color(0xFF831010),
    background = Color(0xFF1C1C1C),
    surface = Color(0xFF1C1C1C),
    error = Color(0xFFCF6679),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color(0xFF1C1C1C)
)