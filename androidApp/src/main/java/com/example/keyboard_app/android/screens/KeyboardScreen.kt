package com.example.keyboard_app.android.screens

import android.content.Context
import android.content.res.Configuration
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.example.keyboard_app.android.KeyboardService
import com.example.keyboard_app.android.R
import com.example.keyboard_app.android.common.KeyboardSizing
import com.example.keyboard_app.android.common.KeyboardSizing.calculateTextSize
import com.example.keyboard_app.android.common.KeyboardSizing.calculatekeyMargin
import com.example.keyboard_app.android.utils.getBorderColor
import com.example.keyboard_app.android.utils.getKeyColor
import com.example.keyboard_app.android.utils.getKeyIconColor
import com.example.keyboard_app.android.utils.getKeyPopupTextColor
import com.example.keyboard_app.android.utils.getKeyTextColor
import com.example.keyboard_app.android.utils.getKeyboardBG
import com.example.keyboard_app.android.utils.getLongPressKeyColor
import com.example.keyboard_app.android.utils.short_vibrate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun KeyboardScreen(getKeys: () -> List<List<String>>) {
    val service = LocalContext.current as? KeyboardService ?: return
    val keys = getKeys()
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp
    val screenHeight = config.screenHeightDp.dp
    val keyHeight = KeyboardSizing.calculateKeyHeight(screenHeight, screenWidth) *
            if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) 1.6f else 1f
    val keyWidth = KeyboardSizing.calculateKeyWidth(screenWidth, keys.firstOrNull()?.size ?: 10)
    val horPadding = KeyboardSizing.calculateHorizontalPadding(screenWidth)
    val vertPadding = KeyboardSizing.calculateVerticalPadding(screenHeight)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(getKeyboardBG())
            .padding(KeyboardSizing.keyMargin)
    ) {
        if (service.isEmojiKeyboard) {
            EmojiKeyboard(service)
        } else {
            keys.forEach { row ->
                Row(Modifier.fillMaxWidth()) {
                    Spacer(Modifier.width(calculatekeyMargin(screenWidth, screenHeight)))
                    row.forEachIndexed { index, key ->
                        KeyboardKey(
                            key = key,
                            horPadding = horPadding,
                            vertPadding = vertPadding,
                            modifier = Modifier
                                .weight(calculateWeights(row)[index])
                                .height(keyHeight)
                                .width(keyWidth)
                        )
                    }
                    Spacer(Modifier.width(calculatekeyMargin(screenWidth, screenHeight)))
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun KeyboardKey(
    key: String,
    horPadding: Dp,
    vertPadding: Dp,
    modifier: Modifier = Modifier
) {
    val ctx = LocalContext.current as? KeyboardService ?: return
    val config = LocalConfiguration.current
    var isPressed by remember { mutableStateOf(false) }
    var showPopup by remember { mutableStateOf(false) }
    var isLongPress by remember { mutableStateOf(false) } // Track long press state
    val coroutineScope = rememberCoroutineScope()
    val activePointers = remember { mutableMapOf<Int, Job>() }
    val isSpecial = key in listOf("language", "←", "⏎", ":)", "↑")
    val textSize = KeyboardSizing.calculateTextSize(config.screenWidthDp.dp, config.screenHeightDp.dp, key.length > 3)

    if (key == " ") {
        Spacer(modifier)
        return
    }

    Box(
        modifier = modifier
            .pointerInteropFilter { event ->
                val pointerId = event.getPointerId(event.actionIndex)
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                        if (!activePointers.containsKey(pointerId)) {
                            isPressed = true
                            showPopup = true
                            isLongPress = false // Reset long press state
                            short_vibrate(ctx)
                            handleShortKeyAction(ctx, key) // Trigger short press immediately
                            activePointers[pointerId] = coroutineScope.launch {
                                delay(400L)
                                if (isPressed && activePointers.containsKey(pointerId)) {
                                    isLongPress = true // Mark as long press
                                    when (key) {
                                        in listOf("←", "⏎", "language") -> startContinuousAction(ctx, key)
                                        else -> handleLongPress(ctx, key)
                                    }
                                }
                            }
                        }
                        true
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                        activePointers.remove(pointerId)?.cancel()
                        isPressed = activePointers.isNotEmpty()
                        showPopup = activePointers.isNotEmpty()
                        isLongPress = activePointers.isNotEmpty() && isLongPress
                        if (activePointers.isEmpty()) stopContinuousAction()
                        true
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        isPressed = false
                        showPopup = false
                        isLongPress = false
                        activePointers.values.forEach { it.cancel() }
                        activePointers.clear()
                        stopContinuousAction()
                        true
                    }
                    else -> false
                }
            }
            .padding(horizontal = horPadding, vertical = vertPadding)
            .clip(RoundedCornerShape(KeyboardSizing.keyCornerRadius))
            .background(getKeyColor(isSpecial))
            .border(1.dp, getBorderColor(), RoundedCornerShape(KeyboardSizing.keyCornerRadius)),
        contentAlignment = Alignment.Center
    ) {
        // Key content
        when {
            key.contains("^") -> DualTextKey(key, config.screenWidthDp.dp, config.screenHeightDp.dp)
            isSpecial && key != "language" -> IconKey(key)
            else -> TextKey(key, textSize)
        }

        // Popup for tapped key
        if (showPopup) {
            Popup(
                alignment = Alignment.TopCenter,
                offset = IntOffset(0, -80), // Slightly closer to the key
                onDismissRequest = { /* No-op since controlled by showPopup */ }
            ) {
                Box(
                    modifier = Modifier
                        .shadow(4.dp, CircleShape)
                        .background(
                            if (isLongPress) Color(0xFF6200EE) else getKeyColor(isSpecial), // Different color for long press
                            CircleShape
                        )
                        .padding(
                            horizontal = if (isLongPress) 12.dp else 8.dp, // Smaller padding for short press
                            vertical = if (isLongPress) 8.dp else 6.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        key.contains("^") -> Text(
                            text = if (isLongPress) key.split("^")[1] else key.split("^")[0], // Show secondary char on long press
                            fontSize = if (isLongPress) 20.sp else 18.sp, // Smaller for short press
                            color = getKeyTextColor(),
                            fontWeight = FontWeight.Bold
                        )
                        isSpecial && key != "language" -> IconKey(key)
                        else -> Text(
                            text = key,
                            fontSize = if (isLongPress) 20.sp else 18.sp,
                            color = getKeyTextColor(),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

private fun calculateWeights(row: List<String>): List<Float> {
    val specialWeights = mapOf("language" to 3.5f, "?1#" to 1.5f, " " to 0.5f, "⏎" to 2f, "↑" to 1.5f, "←" to 1.5f)
    val specialWeightSum = row.sumOf { specialWeights[it]?.toDouble() ?: 0.0 }.toFloat()
    val normalWeight = (10f - specialWeightSum) / (row.count { it !in specialWeights })
    return row.map { specialWeights[it] ?: normalWeight }
}

private fun handleShortKeyAction(ctx: KeyboardService, key: String) {
    when (key) {
        "←" -> ctx.currentInputConnection?.deleteSurroundingText(1, 0)
        "⏎" -> ctx.currentInputConnection?.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
        ":)" -> ctx.switchToEmojiKeyboard()
        "↑" -> ctx.toggleCaps()
        "language" -> ctx.currentInputConnection?.commitText(" ", 1)
        "?1#" -> ctx.switchToNumberKeyboard()
        else -> {
            val text = if (key.contains("^")) key.split("^")[0] else key
            ctx.currentInputConnection?.commitText(
                if (ctx.isCapsEnabled) text.uppercase() else text.lowercase(),
                text.length
            )
        }
    }
}

@Composable
private fun DualTextKey(key: String, screenWidth: Dp, screenHeight: Dp) {
    Box(Modifier.fillMaxWidth().height(60.dp)) {
        Text(
            text = key.split("^")[1],
            fontSize = KeyboardSizing.calculateTextSize(screenWidth, screenHeight, true),
            color = getKeyTextColor(),
            modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
        )
        Text(
            text = key.split("^")[0],
            fontSize = KeyboardSizing.calculateTextSize(screenWidth, screenHeight, false),
            color = getKeyTextColor(),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun IconKey(key: String) {
    Icon(
        painter = painterResource(
            when (key) {
                "←" -> R.drawable.delete_dark
                "⏎" -> R.drawable.enter_dark
                ":)" -> R.drawable.emoji_dark
                "↑" -> R.drawable.arrow_top_dark
                else -> R.drawable.delete_dark
            }
        ),
        contentDescription = key,
        tint = getKeyIconColor(),
        modifier = Modifier.size(KeyboardSizing.calculateIconSize(LocalConfiguration.current.screenWidthDp.dp, LocalConfiguration.current.screenHeightDp.dp))
    )
}

@Composable
private fun TextKey(key: String, textSize: TextUnit) {
    Text(
        text = key,
        fontSize = textSize,
        color = getKeyTextColor(),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

private fun handleLongPress(ctx: KeyboardService, key: String) {
    if (key.contains("^")) {
        val text = key.split("^")[1]
        ctx.currentInputConnection?.commitText(text, text.length)
    }
}

private val continuousActions = mutableMapOf<String, Job>()

private fun startContinuousAction(ctx: KeyboardService, key: String) {
    continuousActions[key]?.cancel()
    continuousActions[key] = GlobalScope.launch {
        while (isActive) {
            when (key) {
                "←" -> ctx.currentInputConnection?.deleteSurroundingText(1, 0)
                "⏎" -> ctx.currentInputConnection?.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
                "language" -> ctx.currentInputConnection?.commitText(" ", 1)
            }
            delay(30)
        }
    }
}

private fun stopContinuousAction() {
    continuousActions.values.forEach { it.cancel() }
    continuousActions.clear()
}