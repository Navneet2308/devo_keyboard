package com.example.keyboard_app.android.screens

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import com.example.keyboard_app.android.MainActivity
import com.example.keyboard_app.android.theming.CurrentBoxColor
import com.example.keyboard_app.android.theming.DarkColorPrimary
import com.example.keyboard_app.android.theming.SelectedCircleColor
import com.example.keyboard_app.android.theming.SetupButtonColor
import com.example.keyboard_app.android.theming.UnSelectedCircleColor
import com.example.keyboard_app.android.theming.appName
import com.goodwy.keyboard.lib.util.InputMethodUtils
import com.goodwy.keyboard.lib.util.InputMethodUtils.showImeEnablerActivity
import com.goodwy.keyboard.lib.util.InputMethodUtils.showImePicker

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun KeyboardSetupScreen(context: Context) {
    var currentStep by remember { mutableStateOf(1) }
    val isFlorisBoardSelected by InputMethodUtils.observeIsKeyboardSelected(foregroundOnly = true)
    val isFlorisBoardEnabled by InputMethodUtils.observeIsKeyboardEnabled(foregroundOnly = true)

    LaunchedEffect(isFlorisBoardEnabled, isFlorisBoardSelected) {
        currentStep = when {
            isFlorisBoardEnabled && isFlorisBoardSelected -> 3
            isFlorisBoardEnabled -> 2
            else -> 1
        }
        if (isFlorisBoardEnabled && currentStep == 2) {
            val backIntent = Intent(context, MainActivity::class.java)
            backIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(backIntent)
            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        }

    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkColorPrimary)
            .padding(16.dp)
            .animateContentSize(animationSpec = tween(500))  // Smooth content size animation
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Stepper animation
        AnimatedVisibility(true) {
            StepItem(
                step = "1",
                title = "Enable",
                description = "${appName} Keyboard in your keyboard settings",
                isActive = currentStep > 1,
                isCurrent = currentStep == 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .animateContentSize()
            )
        }

        AnimatedVisibility(true) {
            StepItem(
                step = "2",
                title = "Select",
                description = "Select ${appName} Keyboard as your default",
                isActive = currentStep > 2,
                isCurrent = currentStep == 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .animateContentSize()
            )
        }

        AnimatedVisibility(true) {
            StepItem(
                step = "3",
                title = "All done!",
                description = "You're all set with ${appName} Keyboard",
                isActive = currentStep > 3,
                isCurrent = currentStep == 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .animateContentSize()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            shape = MaterialTheme.shapes.medium,
            onClick = {
                if (currentStep == 1) {

                    showImeEnablerActivity(context)
                } else if (currentStep == 2) {
                    showImePicker(context)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .animateContentSize(),  // Smooth button animation
            colors = ButtonDefaults.buttonColors(containerColor = SetupButtonColor)
        ) {
            Text(
                text = when (currentStep) {
                    1 -> "Enable"
                    2 -> "Select"
                    3 -> "Start"
                    else -> "Next"
                },
                fontSize = 18.sp,
                color = Color.White
            )
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
    modifier: Modifier = Modifier
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

    Column(
        modifier = modifier
            .background(backgroundColor.value, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
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
        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = title,
            fontSize = if (isCurrent) 24.sp else 20.sp,
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



