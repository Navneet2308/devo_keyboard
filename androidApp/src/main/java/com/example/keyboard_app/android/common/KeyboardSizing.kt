package com.example.keyboard_app.android.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object KeyboardSizing {
    val keyMargin = 2.dp
    val keyCornerRadius = 8.dp

    @Composable
    fun calculateKeyHeight(screenHeight: Dp): Dp {
        return (screenHeight * 0.06f).coerceAtMost(48.dp)
    }

    @Composable
    fun getintroboxcontentModifier(windowSizeClass: WindowWidthSizeClass): Modifier {
        return when (windowSizeClass) {
            WindowWidthSizeClass.Compact -> Modifier.fillMaxSize().padding(horizontal = 5.dp, vertical = 8.dp)
            WindowWidthSizeClass.Medium -> Modifier.fillMaxSize().padding(horizontal = 10.dp, vertical = 15.dp)
            WindowWidthSizeClass.Expanded -> Modifier.padding(horizontal = 10.dp, vertical = 15.dp)
            else -> {
                Modifier.fillMaxSize().padding(horizontal = 10.dp, vertical = 15.dp)
            }
        }
    }

    @Composable
    fun getintrocontentModifier(windowSizeClass: WindowWidthSizeClass): Modifier {
        return when (windowSizeClass) {
            WindowWidthSizeClass.Compact -> Modifier.fillMaxSize().padding(16.dp)
            WindowWidthSizeClass.Medium -> Modifier.fillMaxSize().padding(horizontal = 48.dp)
            WindowWidthSizeClass.Expanded -> Modifier.padding(horizontal = 100.dp)
            else -> {
                Modifier.fillMaxSize().padding(horizontal = 48.dp)
            }
        }


    }
    @Composable
    fun getintrobuttonModifier(windowSizeClass: WindowWidthSizeClass): Modifier {
        return when (windowSizeClass) {
            WindowWidthSizeClass.Compact ->  Modifier.fillMaxWidth().padding(12.dp)
            WindowWidthSizeClass.Medium -> Modifier.fillMaxWidth().padding(12.dp)
            WindowWidthSizeClass.Expanded ->  Modifier.padding(30.dp)
            else -> {
                Modifier.fillMaxWidth().padding(12.dp)
            }
        }


    }


    @Composable
    fun checkIslandscape(windowSizeClass: WindowWidthSizeClass): Boolean {
        return  when (windowSizeClass) {
            WindowWidthSizeClass.Expanded -> true
            else -> {
                false
            }
        }}
    @Composable
    fun getSetupBoxCircleVerticalPadding(windowSizeClass: WindowWidthSizeClass): Dp {
        return  when (windowSizeClass) {
            WindowWidthSizeClass.Compact -> 8.dp
            WindowWidthSizeClass.Medium -> 10.dp
            WindowWidthSizeClass.Expanded -> 15.dp
            else -> {
                10.dp
            }
        }}

    @Composable
    fun getSetupMainTextSize(windowSizeClass: WindowWidthSizeClass): TextUnit {
        return  when (windowSizeClass) {
            WindowWidthSizeClass.Compact -> 14.sp
            WindowWidthSizeClass.Medium -> 18.sp
            WindowWidthSizeClass.Expanded -> 20.sp
            else -> {
                18.sp
            }
        }}

    @Composable
    fun getSetupTitleTextSize(is_current:Boolean,windowSizeClass: WindowWidthSizeClass): TextUnit {
        return  when (windowSizeClass) {
            WindowWidthSizeClass.Compact ->  if (is_current) 20.sp else 16.sp
            WindowWidthSizeClass.Medium ->  if (is_current) 24.sp else 20.sp
            WindowWidthSizeClass.Expanded ->  if (is_current) 28.sp else 24.sp
            else -> {
                if (is_current) 24.sp else 20.sp
            }
        }}



    @Composable
    fun getSetupBoxInternalPadding(windowSizeClass: WindowWidthSizeClass): Dp {
        return  when (windowSizeClass) {
            WindowWidthSizeClass.Compact -> 8.dp
            WindowWidthSizeClass.Medium -> 10.dp
            WindowWidthSizeClass.Expanded -> 15.dp
            else -> {
                10.dp
            }
        }}

    @Composable
    fun getSetupBoxSize(windowSizeClass: WindowWidthSizeClass): Dp {
        return  when (windowSizeClass) {
            WindowWidthSizeClass.Compact -> 35.dp
            WindowWidthSizeClass.Medium -> 40.dp
            WindowWidthSizeClass.Expanded -> 45.dp
            else -> {
                40.dp
            }
        }}

    @Composable
    fun getSetupBoxCorner(windowSizeClass: WindowWidthSizeClass): Dp {
        return  when (windowSizeClass) {
            WindowWidthSizeClass.Compact -> 5.dp
            WindowWidthSizeClass.Medium -> 10.dp
            WindowWidthSizeClass.Expanded -> 15.dp
            else -> {
                10.dp
            }
        }}

    @Composable
    fun calculateKeyWidth(screenWidth: Dp, rowSize: Int): Dp {
        return (screenWidth / rowSize) - (keyMargin * 2)
    }

    @Composable
    fun calculateTextSize(screenWidth: Dp, screenHeight: Dp, isSmall: Boolean = false): TextUnit {
        val baseSize = minOf(screenWidth.value * 0.04f, 18f).sp
        return if (isSmall) (baseSize * 0.8f) else baseSize
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