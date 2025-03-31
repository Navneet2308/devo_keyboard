package com.example.keyboard_app.android.screens

import android.content.Context
import android.os.Handler
import android.os.Looper

import android.view.KeyEvent
import android.view.MotionEvent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyboard_app.android.KeyboardService
import com.example.keyboard_app.android.R
import com.example.keyboard_app.android.utils.getBorderColor
import com.example.keyboard_app.android.utils.getKeyColor
import com.example.keyboard_app.android.utils.getKeyIconColor
import com.example.keyboard_app.android.utils.getKeyTextColor
import com.example.keyboard_app.android.utils.getKeyboardBG
import com.example.keyboard_app.android.utils.short_vibrate
import kotlinx.coroutines.launch

private var activePointers = mutableMapOf<Int, String>()
private var continuousActionJob: kotlinx.coroutines.Job? = null

@Composable
fun KeyboardScreen(getKeys: () -> List<List<String>>) {
    val service = LocalContext.current as? KeyboardService ?: return
    val keys = getKeys()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(getKeyboardBG())
            .padding(8.dp)
    ) {
        if (service._isEmojiKeyboard.value) {
            EmojiKeyboard(service)
        } else {
            keys.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    val weights = calculateWeights(row)
                    row.forEachIndexed { index, key ->
                        KeyboardKey(key, Modifier.weight(weights[index]).height(48.dp))
                    }
                }
            }
        }
    }
}


fun calculateWeights(row: List<String>, totalWeight: Float = 10f): List<Float> {
    val specialWeights =
        mapOf("language" to 3.5f, "?1#" to 1.5f, " " to 0.5f, "⏎" to 2f, "↑" to 1.5f, "←" to 1.5f)
    val specialKeys = row.filter { it in specialWeights }
    val specialWeightSum = specialKeys.fold(0f) { acc, key -> acc + (specialWeights[key] ?: 0f) }
    val normalKeys = row.size - specialKeys.size
    val normalWeight =
        if (normalKeys > 0) (totalWeight - specialWeightSum) / normalKeys else totalWeight

    return row.map { specialWeights[it] ?: normalWeight }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun KeyboardKey(key: String, modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    var isKeyPressed by remember { mutableStateOf(false) }
    var isLongPressStarted by remember { mutableStateOf(false) }
    val isSpecial = key in listOf("language", "←", "⏎", ":)", "↑")
    val isSmallText = key.length > 3 || key == "English (United Sta..."
    val coroutineScope = rememberCoroutineScope()
    if (key.contains(" ")) {
        Spacer(modifier = modifier)
    } else {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(getKeyColor(isSpecial))
                .scale(if (isKeyPressed) 0.95f else 1f)
                .pointerInteropFilter { event ->
                    when (event.actionMasked) {
                        MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                            val pointerId = event.getPointerId(event.actionIndex)
                            activePointers[pointerId] = key
                            isKeyPressed = true
                            short_vibrate(ctx)

                            if (key.contains("^")) {
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(1000)
                                    if (isKeyPressed) {
                                        isLongPressStarted = true
                                        handleLongKeyAction(ctx, key)
                                    }
                                }
                            } else if (key in listOf("←", "⏎", "language")) {
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(1000)
                                    if (isKeyPressed) {
                                        isLongPressStarted = true
                                        startContinuousAction(ctx, key)
                                    }
                                }
                            } else {
                                handleKeyAction(ctx, key)
                            }
                            true
                        }

                        MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                            val pointerId = event.getPointerId(event.actionIndex)
                            activePointers.remove(pointerId)
                            if (activePointers.isEmpty()) {
                                isKeyPressed = false
                                if (key.contains("^") && !isLongPressStarted) {
                                    handleShortPressAction(ctx, key)
                                } else if (key in listOf(
                                        "←",
                                        "⏎",
                                        "language"
                                    ) && !isLongPressStarted
                                ) {
                                    handleKeyAction(ctx, key)
                                }
                                isLongPressStarted = false
                                stopContinuousAction()
                            }
                            true
                        }

                        MotionEvent.ACTION_CANCEL -> {
                            activePointers.clear()
                            isKeyPressed = false
                            isLongPressStarted = false
                            stopContinuousAction()
                            true
                        }

                        else -> false
                    }
                }
                .border(1.dp, getBorderColor(), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            when {
                key.contains("^") -> DualTextKey(key)
                key in listOf("←", "⏎", ":)", "↑") -> IconKey(key, isSmallText)
                else -> TextKey(key, isSmallText)
            }
        }
    }
}

fun handleLongKeyAction(ctx: Context, key: String) {
    val service = ctx as? KeyboardService ?: return
    if (key.contains("^")) {
        val secondChar = key.split("^")[1]
        service.currentInputConnection?.commitText(secondChar, secondChar.length)
    }
}

fun handleShortPressAction(ctx: Context, key: String) {
    val service = ctx as? KeyboardService ?: return
    if (key.contains("^")) {
        val firstChar = key.split("^")[0]
        service.currentInputConnection?.commitText(firstChar, firstChar.length)
    }
}

@Composable
fun DualTextKey(key: String) {
    Box(Modifier.fillMaxWidth().height(60.dp)) {
        Text(
            text = key.split("^")[1], fontSize = 10.sp,
            color = getKeyTextColor(), modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
        )
        Text(
            text = key.split("^")[0], fontSize = 18.sp,
            color = getKeyTextColor(), modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun IconKey(key: String, isSmallText: Boolean) {
    val icon = when (key) {
        "←" -> R.drawable.delete_dark
        "⏎" -> R.drawable.enter_dark
        ":)" -> R.drawable.emoji_dark
        "↑" -> R.drawable.arrow_top_dark
        else -> null
    }
    icon?.let {
        Icon(
            painter = painterResource(id = it),
            contentDescription = key,
            tint = getKeyIconColor(),
            modifier = Modifier.size(if (isSmallText) 18.dp else 24.dp)
        )
    }
}

@Composable
fun TextKey(key: String, isSmallText: Boolean) {
    Text(
        text = key,
        fontSize = if (isSmallText) 14.sp else 18.sp,
        color = getKeyTextColor(),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

fun handleKeyAction(ctx: Context, key: String) {
    val service = ctx as? KeyboardService ?: return

    when (key) {
        "←" -> {
            service.currentInputConnection?.deleteSurroundingText(1, 0)
        }

        "⏎" -> {
            service.currentInputConnection?.sendKeyEvent(
                KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER)
            )
            service.currentInputConnection?.sendKeyEvent(
                KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER)
            )
        }

        ":)" -> {
            service.switchToEmojiKeyboard()
        }

        "↑" -> service.toggleCaps()
        "language" -> {
            service.currentInputConnection?.commitText(" ", 1)
        }

        "?1#" -> {
            service.switchToNumberKeyboard()
        }

        else -> {
            val textToCommit = if (service.isCapsEnabled) key.uppercase() else key.lowercase()
            service.currentInputConnection?.commitText(textToCommit, textToCommit.length)
        }
    }
}

fun startContinuousAction(ctx: Context, key: String) {
    stopContinuousAction()
    continuousActionJob = kotlinx.coroutines.GlobalScope.launch {
        while (true) {
            when (key) {
                "←" -> {
                    val service = ctx as? KeyboardService ?: return@launch
                    service.currentInputConnection?.deleteSurroundingText(1, 0)
                }

                "⏎" -> {
                    val service = ctx as? KeyboardService ?: return@launch
                    service.currentInputConnection?.sendKeyEvent(
                        KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER)
                    )
                    service.currentInputConnection?.sendKeyEvent(
                        KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER)
                    )
                }

                "language" -> {
                    val service = ctx as? KeyboardService ?: return@launch
                    service.currentInputConnection?.commitText(" ", 1)
                }
            }
            kotlinx.coroutines.delay(50) // 50ms delay between actions
        }
    }
}

fun stopContinuousAction() {
    continuousActionJob?.cancel()
    continuousActionJob = null
}
