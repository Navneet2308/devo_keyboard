package com.example.keyboard_app.android.screens

import android.content.Context
import android.content.res.Configuration
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.keyboard_app.android.common.Key.getKeyboardKeys
import com.example.keyboard_app.android.common.KeyboardSizing
import com.example.keyboard_app.android.common.KeyboardSizing.calculateTextSize
import com.example.keyboard_app.android.common.KeyboardSizing.calculatekeyMargin
import com.example.keyboard_app.android.ime.KeyboardType
import com.example.keyboard_app.android.theming.DarkColorPrimary
import com.example.keyboard_app.android.utils.getBorderColor
import com.example.keyboard_app.android.utils.getKeyColor
import com.example.keyboard_app.android.utils.getKeyIconColor
import com.example.keyboard_app.android.utils.getKeyPopupTextColor
import com.example.keyboard_app.android.utils.getKeyTextColor
import com.example.keyboard_app.android.utils.getKeyboardBG
import com.example.keyboard_app.android.utils.short_vibrate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun KeyboardScreen() {
    var keyboardType by remember { mutableStateOf(KeyboardType.LETTERS) }
    val service = LocalContext.current as? KeyboardService ?: return
    val keys by remember(
        keyboardType,
        service.isNextLetterCaps.value,
        service.isCapsEnabled.value
    ) {
        mutableStateOf(
            getKeyboardKeys(
                service.isNextLetterCaps.value,
                service.isCapsEnabled.value,
                keyboardType
            )
        )
    }
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp
    val screenHeight = config.screenHeightDp.dp

    val keyHeight by remember(screenHeight, screenWidth, config.orientation) {
        mutableStateOf(
            KeyboardSizing.calculateKeyHeight(screenHeight, config.orientation) *
                    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) 1.6f else 1f
        )
    }

    val keyWidth by remember(screenWidth, keys) {
        mutableStateOf(
            KeyboardSizing.calculateKeyWidth(
                screenWidth,
                keys.firstOrNull()?.size ?: 10
            )
        )
    }
    val horPadding by remember(screenWidth) {
        mutableStateOf(KeyboardSizing.calculateHorizontalPadding(screenWidth))
    }
    val vertPadding by remember(screenHeight) {
        mutableStateOf(KeyboardSizing.calculateVerticalPadding(screenHeight))
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(getKeyboardBG())
            .padding(KeyboardSizing.keyMargin)
    ) {
        when (keyboardType) {
            KeyboardType.EMOJI -> EmojiKeyboard(service, onKeyboardTypeChange = { newType ->
                keyboardType = newType
            })


            KeyboardType.LETTERS, KeyboardType.NUMBERS2, KeyboardType.NUMBERS -> {
                keys.forEach { row ->
                    Row(Modifier.fillMaxWidth()) {
                        Spacer(Modifier.width(calculatekeyMargin(screenWidth, screenHeight)))
                        row.forEachIndexed { index, key ->
                            KeyboardKey(
                                keyboardType = keyboardType,
                                key = key,
                                horPadding = horPadding,
                                vertPadding = vertPadding,
                                modifier = Modifier
                                    .weight(calculateWeights(row)[index])
                                    .height(keyHeight)
                                    .width(keyWidth),
                                onKeyboardTypeChange = { newType ->
                                    keyboardType = newType
                                }
                            )
                        }
                        Spacer(Modifier.width(calculatekeyMargin(screenWidth, screenHeight)))
                    }
                }
            }
        }

    }

}

@Composable
fun KeyboardKey(
    keyboardType: KeyboardType,
    key: String,
    horPadding: Dp,
    vertPadding: Dp,
    modifier: Modifier = Modifier,
    onKeyboardTypeChange: (KeyboardType) -> Unit = {}
) {
    val ctx = LocalContext.current as? KeyboardService ?: return
    val config = LocalConfiguration.current
    var isPressed by remember { mutableStateOf(false) }
    var showPopup by remember { mutableStateOf(false) }
    var isLongPressActive by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val isSpecial = key in listOf("language", "←", "⏎", ":)", "↑")
    val textSize = KeyboardSizing.calculateTextSize(
        config.screenWidthDp.dp,
        config.screenHeightDp.dp,
        key.length > 5
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 50)
    )

    Box(
        modifier = modifier
            .scale(scale)
            .pointerInput(keyboardType) {
                detectTapGestures(
                    onPress = { event ->
                        isPressed = true
                        showPopup = true
                        short_vibrate(ctx)
                        val job = coroutineScope.launch {
                            delay(400L)
                            if (isPressed) {
                                isLongPressActive = true
                                when (key) {
                                    in listOf("←", "⏎", "language") -> startContinuousAction(
                                        ctx,
                                        key
                                    )

                                    else -> handleLongPress(ctx, key)
                                }
                            }
                        }
                        tryAwaitRelease()
                        job.cancel()
                        if (isLongPressActive) {
                            stopContinuousAction()
                            isLongPressActive = false
                        }
                        isPressed = false
                        showPopup = false
                        handleShortKeyAction(keyboardType, ctx, key, onKeyboardTypeChange)

                    }
                )
            }
            .padding(horizontal = horPadding, vertical = vertPadding)
            .clip(RoundedCornerShape(KeyboardSizing.keyCornerRadius))
            .background(
                when {
                    isPressed -> Color.Gray.copy(alpha = 0.5f) // Change color when pressed
                    key == "↑" && (ctx.isCapsEnabled.value || ctx.isNextLetterCaps.value) -> getKeyPopupTextColor()
                    else -> getKeyColor(isSpecial)
                }
            )
            .border(1.dp, getBorderColor(), RoundedCornerShape(KeyboardSizing.keyCornerRadius)),
        contentAlignment = Alignment.Center
    ) {
        when {
            key == " " -> Spacer(modifier = Modifier)
            key.contains("^") -> DualTextKey(key, config.screenWidthDp.dp, config.screenHeightDp.dp)
            isSpecial && key != "language" -> IconKey(key)
            else -> TextKey(key, textSize)
        }

        if (showPopup) {
            Popup(
                alignment = Alignment.TopCenter,
                offset = IntOffset(0, -80),
                onDismissRequest = {}
            ) {
                Box(
                    modifier = Modifier
                        .shadow(4.dp, CircleShape)
                        .background(
                            if (isLongPressActive) DarkColorPrimary else getKeyColor(isSpecial),
                            CircleShape
                        )
                        .padding(
                            horizontal = if (isLongPressActive) 20.dp else 20.dp,
                            vertical = if (isLongPressActive) 20.dp else 20.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        key.contains("^") -> Text(
                            text = if (isLongPressActive) key.split("^")[1] else key.split("^")[0],
                            fontSize = if (isLongPressActive) 18.sp else 18.sp,
                            color = getKeyTextColor(isLongPressActive),
                            fontWeight = FontWeight.Bold
                        )

                        isSpecial && key != "language" -> IconKey(key)
                        else -> Text(
                            text = key,
                            fontSize = if (isLongPressActive) 20.sp else 18.sp,
                            color = getKeyTextColor(isLongPressActive),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

private fun calculateWeights(row: List<String>): List<Float> {
    val specialWeights =
        mapOf(
            "language" to 3.5f,
            "?1#" to 1.5f,
            "=\\\\<" to 1.5f,
            "ABC" to 1.5f,
            "?123" to 1.5f,
            " " to 0.5f,
            "⏎" to 2f,
            "↑" to 1.5f,
            "←" to 1.5f
        )
    val specialWeightSum = row.sumOf { specialWeights[it]?.toDouble() ?: 0.0 }.toFloat()
    val normalWeight = (10f - specialWeightSum) / (row.count { it !in specialWeights })
    return row.map { specialWeights[it] ?: normalWeight }
}

private fun handleShortKeyAction(
    keyboardType: KeyboardType,
    ctx: KeyboardService,
    key: String,
    onKeyboardTypeChange: (KeyboardType) -> Unit
) {
    println("Key pressed: $key, Current type: $keyboardType") // Debug log
    when (key) {
        "←" -> ctx.currentInputConnection?.deleteSurroundingText(1, 0)
        "⏎" -> ctx.currentInputConnection?.sendKeyEvent(
            KeyEvent(
                KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_ENTER
            )
        )

        ":)" -> onKeyboardTypeChange(KeyboardType.EMOJI)
        "↑" -> ctx.toggleCaps()
        "language" -> ctx.currentInputConnection?.commitText(" ", 1)
        "?1#" -> {
            onKeyboardTypeChange(KeyboardType.NUMBERS)
        }

        "=\\\\<" -> {
            onKeyboardTypeChange(KeyboardType.NUMBERS2)
        }

        "?123" -> {
            onKeyboardTypeChange(KeyboardType.NUMBERS)
        }

        "ABC" -> {
            onKeyboardTypeChange(KeyboardType.LETTERS)
        }

        else -> {
            if (keyboardType == KeyboardType.NUMBERS || keyboardType == KeyboardType.NUMBERS2) {
                ctx.currentInputConnection?.commitText(key, key.length)
            } else {
                val text = if (key.contains("^")) key.split("^")[0] else key
                ctx.currentInputConnection?.commitText(
                    if (ctx.isCapsEnabled.value || ctx.isNextLetterCaps.value) text.uppercase() else text.lowercase(),
                    text.length
                )
                if (!key.contains("↑")) {
                    ctx.changeNextLetterCaps()
                }
            }
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
        modifier = Modifier.size(
            KeyboardSizing.calculateIconSize(
                LocalConfiguration.current.screenWidthDp.dp,
                LocalConfiguration.current.screenHeightDp.dp
            )
        )
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
        var repeatDelay = 200L // Initial delay
        while (isActive) {
            when (key) {
                "←" -> ctx.currentInputConnection?.deleteSurroundingText(1, 0)
                "⏎" -> ctx.currentInputConnection?.sendKeyEvent(
                    KeyEvent(
                        KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_ENTER
                    )
                )

                "language" -> ctx.currentInputConnection?.commitText(" ", 1)
            }
            delay(repeatDelay)
            repeatDelay = 50L // Faster repeat rate after initial delay
        }
    }
}

private fun stopContinuousAction() {
    continuousActions.values.forEach { it.cancel() }
    continuousActions.clear()
}
