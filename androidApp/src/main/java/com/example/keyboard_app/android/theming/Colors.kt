package com.example.keyboard_app.android.theming

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val DarkColorPrimary = Color(0xFF001F3F)
val LightColorButton = Color(0xFFB0B0B0)
val LightBackground = Color(0xFFF8F8F8)
val LightSurface = Color(0xFFFFFFFF)
val LightTextColor = Color(0xFF333333)

val SelectedCircleColor= Color(0xFF121212)
val UnSelectedCircleColor= Color(0xFFD3D3D3)
val CurrentBoxColor= Color(0xFFFFE082)
val SetupButtonColor= Color(0xFF4A4A4A)






val WhitePrimary = Color(0xFFFFFFFF)
val DarkButton = Color(0xFF4A4A4A)
val DarkBackground = Color(0xFF1C1C1C)
val DarkSurface = Color(0xFF2A2A2A)
val DarkTextColor = Color(0xFFE0E0E0)

internal val LightColorScheme = lightColorScheme(
    primary = DarkColorPrimary,
    background = LightBackground,
    onBackground = LightTextColor,
    surface = LightSurface,
    onSurface = DarkColorPrimary,
    secondary = LightColorButton,
    onSecondary = LightTextColor
)

internal val DarkColorScheme = darkColorScheme(
    primary = WhitePrimary,
    background = DarkBackground,
    onBackground = DarkTextColor,
    surface = DarkSurface,
    onSurface = WhitePrimary,
    secondary = DarkButton,
    onSecondary = DarkTextColor
)
