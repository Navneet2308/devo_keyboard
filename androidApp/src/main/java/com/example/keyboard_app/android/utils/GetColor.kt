package com.example.keyboard_app.android.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.keyboard_app.android.theming.DarkColorPrimary
import com.example.keyboard_app.android.theming.WhitePrimary

@Composable
fun getKeyColor(isSpecial: Boolean): Color {
    val isDarkMode = isSystemInDarkTheme()
    return when {
        isDarkMode && isSpecial -> Color(0xFF1C4A57)
        isDarkMode && !isSpecial -> Color(0xFF333333)
        !isDarkMode && isSpecial -> Color(0xFF60a6d2ff)
        else -> Color.White
    }
}

@Composable
fun getLongPressKeyColor(): Color {
    val isDarkMode = isSystemInDarkTheme()
    return when {
        isDarkMode -> WhitePrimary
        !isDarkMode -> DarkColorPrimary
        else -> WhitePrimary
    }
}


@Composable
fun getKeyboardBG(): Color {
    val isDarkMode = isSystemInDarkTheme()
    return when {
        isDarkMode -> Color.Black
        else -> Color(0xFFD9D9D9)
    }
}

@Composable
fun getKeyTextColor(): Color {
    val isDarkMode = isSystemInDarkTheme()
    return when {
        isDarkMode -> Color.White
        else -> Color(0xFF555555)
    }
}

@Composable
fun getKeyPopupTextColor(): Color {
    val isDarkMode = isSystemInDarkTheme()
    return when {
        isDarkMode -> Color(0xFF555555)
        else -> Color.White
    }
}

@Composable
fun getKeyIconColor(): Color {
    val isDarkMode = isSystemInDarkTheme()
    return when {
        isDarkMode -> Color.White
        else -> Color(0xFF555555)
    }
}


@Composable
fun getBorderColor(): Color {
    val isDarkMode = isSystemInDarkTheme()
    return if (isDarkMode) Color(0xFF777777) else Color.LightGray
}