package com.example.keyboard_app.android.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object KeyboardSizing {
    val keyCornerRadius = 8.dp

    val keyMargin = 2.dp

    @Composable
    fun calculateKeyHeight(screenHeight: Dp, screenWidth: Dp): Dp {
        val configuration = LocalConfiguration.current
        val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

        return if (isPortrait) {
            (screenHeight * 0.065f).coerceIn(25.dp, 63.dp)
        } else {
            (screenHeight * 0.07f).coerceIn(20.dp, 55.dp) // Adjust height for landscape
        }
    }

    @Composable
    fun calculatekeyMargin(screenWidth: Dp, screenHeight: Dp): Dp {
//        return (screenWidth * 0.02f).coerceIn(2.dp, 20.dp)
        return 0.dp
    }

    @Composable
    fun calculateHorizontalPadding(screenWidth: Dp): Dp {
        return (screenWidth * 0.005f).coerceIn(1.dp, 3.dp)
    }


    @Composable
    fun calculateVerticalPadding(screenHeight: Dp): Dp {
        return (screenHeight * 0.0030f).coerceIn(1.dp, 3.dp)
    }


    @Composable
    fun calculateKeyWidth(screenWidth: Dp, rowSize: Int): Dp {
        return (screenWidth / rowSize) - (keyMargin * 2)
    }

    @Composable
    fun calculateTextSize(screenWidth: Dp, screenHeight: Dp, isSmall: Boolean = false): TextUnit {
        val widthFactor = screenWidth.value * 0.065f
        val heightFactor = screenHeight.value * 0.05f
        val baseSize = maxOf(minOf(widthFactor, heightFactor, 22f), 14f).sp  // Ensures text is between 14.sp and 22.sp
        return if (isSmall) (baseSize * 0.45f) else baseSize
    }
    @Composable
    fun calculateIconSize(screenWidth: Dp, screenHeight: Dp): Dp {
        return (screenWidth * 0.06f).coerceAtMost(24.dp)
    }

    @Composable
    fun calculateDualTextSize(screenWidth: Dp, screenHeight: Dp): Pair<TextUnit, TextUnit> {
        val baseSize = calculateTextSize(screenWidth, screenHeight)
        return Pair(baseSize, baseSize * 0.6f)
    }
}