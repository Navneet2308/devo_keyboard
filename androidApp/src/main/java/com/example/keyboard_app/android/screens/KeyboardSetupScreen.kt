package com.example.keyboard_app.android.screens

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import com.example.keyboard_app.android.MainActivity
import com.example.keyboard_app.android.common.KeyboardSizing.checkIslandscape
import com.example.keyboard_app.android.common.KeyboardSizing.getSetupBoxCircleVerticalPadding
import com.example.keyboard_app.android.common.KeyboardSizing.getSetupBoxCorner
import com.example.keyboard_app.android.common.KeyboardSizing.getSetupBoxInternalPadding
import com.example.keyboard_app.android.common.KeyboardSizing.getSetupBoxSize
import com.example.keyboard_app.android.common.KeyboardSizing.getSetupMainTextSize
import com.example.keyboard_app.android.common.KeyboardSizing.getSetupTitleTextSize
import com.example.keyboard_app.android.common.KeyboardSizing.getintroboxcontentModifier
import com.example.keyboard_app.android.common.KeyboardSizing.getintrobuttonModifier
import com.example.keyboard_app.android.common.KeyboardSizing.getintrocontentModifier
import com.example.keyboard_app.android.theming.CurrentBoxColor
import com.example.keyboard_app.android.theming.DarkColorPrimary
import com.example.keyboard_app.android.theming.SelectedCircleColor
import com.example.keyboard_app.android.theming.SetupButtonColor
import com.example.keyboard_app.android.theming.UnSelectedCircleColor
import com.example.keyboard_app.android.theming.appName
import com.goodwy.keyboard.lib.util.InputMethodUtils
import com.goodwy.keyboard.lib.util.InputMethodUtils.showImeEnablerActivity
import com.goodwy.keyboard.lib.util.InputMethodUtils.showImePicker
import splitties.systemservices.wifiAwareManager

@Composable
fun KeyboardSetupScreen(context: Context, windowSizeClass: WindowWidthSizeClass) {
    var currentStep by remember { mutableStateOf(1) }
    val isFlorisBoardSelected by InputMethodUtils.observeIsKeyboardSelected(foregroundOnly = true)
    val isFlorisBoardEnabled by InputMethodUtils.observeIsKeyboardEnabled(foregroundOnly = true)

    LaunchedEffect(isFlorisBoardEnabled, isFlorisBoardSelected) {
        currentStep = when {
            isFlorisBoardEnabled && isFlorisBoardSelected -> 3
            isFlorisBoardEnabled -> 2
            else -> 1
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkColorPrimary) // Apply background to the Box
    )
    {
        if(checkIslandscape(windowSizeClass)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = getintrocontentModifier(windowSizeClass)
                        .background(DarkColorPrimary)
                        .animateContentSize(animationSpec = tween(500))
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    StepItem(
                        step = "1", title = "Enable",
                        description = "${appName} Keyboard in your keyboard settings",
                        isActive = currentStep > 1, isCurrent = currentStep == 1,
                        windowSizeClass = windowSizeClass
                    )

                    StepItem(
                        step = "2", title = "Select",
                        description = "Select ${appName} Keyboard as your default",
                        isActive = currentStep > 2, isCurrent = currentStep == 2,
                        windowSizeClass = windowSizeClass

                    )

                    StepItem(
                        step = "3", title = "All done!",
                        description = "You're all set with ${appName} Keyboard",
                        isActive = currentStep > 3, isCurrent = currentStep == 3,
                        windowSizeClass = windowSizeClass

                    )

                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    shape = MaterialTheme.shapes.medium,
                    onClick = {
                        if (currentStep == 1) showImeEnablerActivity(context)
                        else if (currentStep == 2) showImePicker(context)
                    },
                    modifier = getintrobuttonModifier(windowSizeClass),
                    colors = ButtonDefaults.buttonColors(containerColor = SetupButtonColor)
                ) {
                    Text(
                        text = if (currentStep == 1) "Enable" else if (currentStep == 2) "Select" else "Start",
                        fontSize = getSetupMainTextSize(windowSizeClass),
                        color = Color.White
                    )
                }
            }

        }
        else
        {
            Column (
                modifier = getintrocontentModifier(windowSizeClass)
                    .verticalScroll(rememberScrollState())
                    .background(DarkColorPrimary)
                    .animateContentSize(animationSpec = tween(500))
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                StepItem(
                    step = "1", title = "Enable",
                    description = "${appName} Keyboard in your keyboard settings",
                    isActive = currentStep > 1, isCurrent = currentStep == 1,
                    windowSizeClass = windowSizeClass
                )

                StepItem(
                    step = "2", title = "Select",
                    description = "Select ${appName} Keyboard as your default",
                    isActive = currentStep > 2, isCurrent = currentStep == 2,
                    windowSizeClass = windowSizeClass

                )

                StepItem(
                    step = "3", title = "All done!",
                    description = "You're all set with ${appName} Keyboard",
                    isActive = currentStep > 3, isCurrent = currentStep == 3,
                    windowSizeClass = windowSizeClass

                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    shape = MaterialTheme.shapes.medium,
                    onClick = {
                        if (currentStep == 1) showImeEnablerActivity(context)
                        else if (currentStep == 2) showImePicker(context)
                    },
                    modifier = getintrobuttonModifier(windowSizeClass),
                    colors = ButtonDefaults.buttonColors(containerColor = SetupButtonColor)
                ) {
                    Text(
                        text = if (currentStep == 1) "Enable" else if (currentStep == 2) "Select" else "Start",
                        fontSize = getSetupMainTextSize(windowSizeClass),
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun StepItem(
    step: String,
    title: String,
    description: String,
    isActive: Boolean,
    isCurrent: Boolean,
    windowSizeClass: WindowWidthSizeClass
) {
    val backgroundColor = animateColorAsState(
        targetValue = if (isCurrent) CurrentBoxColor else Color.Transparent,
        animationSpec = tween(500)
    )

    val stepColor = animateColorAsState(
        targetValue = when {
            isCurrent -> CurrentBoxColor
            isActive -> SelectedCircleColor
            else -> Color.DarkGray
        },
        animationSpec = tween(500)
    )
    val textColor = animateColorAsState(
        targetValue = when {
            isCurrent -> SelectedCircleColor
            isActive -> UnSelectedCircleColor
            else -> UnSelectedCircleColor
        },
        animationSpec = tween(500)
    )

    Box(modifier = getintroboxcontentModifier(windowSizeClass)
        .background(
            backgroundColor.value,
            RoundedCornerShape(getSetupBoxCorner(windowSizeClass))
        ))
    {
        Column(
            modifier = Modifier.padding(getSetupBoxInternalPadding(windowSizeClass))
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = getSetupBoxCircleVerticalPadding(windowSizeClass))
                    .size(getSetupBoxSize(windowSizeClass))
                    .background(
                        if (isCurrent) SelectedCircleColor else UnSelectedCircleColor,
                        RoundedCornerShape(50)
                    )
                    .animateContentSize(),
                contentAlignment = Alignment.Center
            ) {
                if (isActive) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check Icon",
                        tint = stepColor.value
                    )
                } else {
                    Text(
                        text = step,
                        color = stepColor.value,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(
                text = title,
                fontSize =getSetupTitleTextSize(isCurrent,windowSizeClass),
                fontWeight = FontWeight.SemiBold,
                color = textColor.value
            )
            if (isCurrent) {
                Text(
                    text = description,
                    fontSize = 16.sp,
                    color = SelectedCircleColor,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}



