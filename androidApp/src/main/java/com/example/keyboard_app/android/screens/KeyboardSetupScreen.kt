package com.example.keyboard_app.android.screens

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
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay

@Composable
fun KeyboardSetupScreen() {
    var currentStep by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF918BFF))  // Light purple background
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Step 1
        StepItem(
            step = "1",
            title = "Enable",
            description = "Yandex Keyboard in your keyboard settings",
            isActive = currentStep >= 1,
            isCurrent = currentStep == 1,
            onClick = { currentStep = 1 }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Step 2
        StepItem(
            step = "2",
            title = "Select",
            description = "Select Yandex Keyboard as your default",
            isActive = currentStep >= 2,
            isCurrent = currentStep == 2,
            onClick = { currentStep = 2 }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Step 3
        StepItem(
            step = "3",
            title = "All done!",
            description = "You're all set with Yandex Keyboard",
            isActive = currentStep >= 3,
            isCurrent = currentStep == 3,
            onClick = { currentStep = 3 }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { currentStep = (currentStep % 3) + 1 },  // Toggle steps
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF424242))
        ) {
            Text(text = "Next", fontSize = 18.sp, color = Color.White)
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
    onClick: () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(scale) {
        if (scale < 1f) {
            delay(100)
            scale = 1f
        }
    }

    val size = if (isCurrent) 56.dp else 40.dp
    val backgroundColor = when {
        isCurrent -> Color(0xFF4CAF50)   // Green for current step
        isActive -> Color(0xFFBDBDBD)    // Gray for previous steps
        else -> Color.LightGray
    }
    val textColor = if (isCurrent) Color.White else Color.DarkGray
    val titleSize = if (isCurrent) 24.sp else 20.sp
    val descriptionColor = if (isCurrent) Color.White else Color.Gray

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(300, easing = FastOutSlowInEasing))
            .background(
                if (isCurrent) Color(0xFFFFE082) else Color.Transparent,
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(color = Color.Gray)
            ) {
                scale = 0.9f  // Shrink effect on click
                onClick()
            }
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .background(backgroundColor, RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = step,
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                fontSize = titleSize,
                fontWeight = FontWeight.Bold,
                color = if (isActive) Color.Black else Color.Gray
            )
            if (isCurrent) {
                Text(
                    text = description,
                    fontSize = 16.sp,
                    color = descriptionColor,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

