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


    fun calculateKeyHeight(screenHeight: Dp, orientation: Int): Dp {
        return if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            (screenHeight * 0.065f).coerceIn(25.dp, 63.dp)
        } else {
            (screenHeight * 0.07f).coerceIn(20.dp, 55.dp)
        }
    }




    fun calculateKeyWidth(screenWidth: Dp, rowSize: Int): Dp {
        return (screenWidth / rowSize) - (keyMargin * 2)
    }

    fun calculatekeyMargin(screenWidth: Dp, screenHeight: Dp): Dp {
        return 0.dp
    }

    fun calculateHorizontalPadding(screenWidth: Dp): Dp {
        return (screenWidth * 0.004f).coerceIn(1.dp, 3.dp)
    }


    fun calculateVerticalPadding(screenHeight: Dp): Dp {
        return (screenHeight * 0.002f).coerceIn(1.dp, 3.dp)
    }



    @Composable
    fun calculateTextSize(screenWidth: Dp, screenHeight: Dp, isSmall: Boolean = false): TextUnit {
        val widthFactor = screenWidth.value * 0.060f
        val heightFactor = screenHeight.value * 0.04f
        val baseSize = maxOf(minOf(widthFactor, heightFactor, 20f), 12f).sp  // Ensures text is between 14.sp and 22.sp
        return if (isSmall) (baseSize * 0.55f) else baseSize
    }
    @Composable
    fun calculateIconSize(screenWidth: Dp, screenHeight: Dp): Dp {
        return (screenWidth * 0.06f).coerceAtMost(24.dp)
    }


}