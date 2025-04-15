package com.example.keyboard_app.android.screens

import android.content.res.Configuration
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.keyboard_app.android.common.Key.SPECIAL_ABCKEY
import com.example.keyboard_app.android.common.Key.SPECIAL_ARROW_RIGHT
import com.example.keyboard_app.android.common.Key.SPECIAL_ARROW_TOP
import com.example.keyboard_app.android.common.Key.SPECIAL_BACK
import com.example.keyboard_app.android.common.Key.SPECIAL_CHANGESPE
import com.example.keyboard_app.android.common.Key.SPECIAL_DASH
import com.example.keyboard_app.android.common.Key.SPECIAL_DOUBLE_DASH
import com.example.keyboard_app.android.common.Key.SPECIAL_EMOJI
import com.example.keyboard_app.android.common.Key.SPECIAL_ENTER
import com.example.keyboard_app.android.common.Key.SPECIAL_HASH_STAR
import com.example.keyboard_app.android.common.Key.SPECIAL_LANGUAGE
import com.example.keyboard_app.android.common.Key.SPECIAL_NUMKEY
import com.example.keyboard_app.android.common.Key.SPECIAL_QNUMKEY
import com.example.keyboard_app.android.common.Key.SPECIAL_QONEHASH
import com.example.keyboard_app.android.common.Key.SPECIAL_ZERO_PLUSH
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
    val service = LocalContext.current as? KeyboardService ?: return
    var keyboardType by remember { mutableStateOf(service.keyboardType.value) }

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

            KeyboardType.LETTERS, KeyboardType.NUMBERS2, KeyboardType.NUMBERS,KeyboardType.PURENUMBERS, KeyboardType.SEMIPURENUMBERS -> {
                keys.forEach { row ->
                    Row(Modifier.fillMaxWidth()) {
                        Spacer(Modifier.width(calculatekeyMargin(screenWidth, screenHeight)))
                        row.forEachIndexed { index, key ->
                            if (keyboardType == KeyboardType.PURENUMBERS) {
                                KeyboardKey(
                                    keyboardType = keyboardType,
                                    key = key,
                                    horPadding = horPadding,
                                    vertPadding = vertPadding,
                                    modifier = Modifier
                                        .weight(calculateWeights2(row)[index])
                                        .height(keyHeight)
                                        .width(keyWidth),
                                    onKeyboardTypeChange = { newType ->
                                        keyboardType = newType
                                    }
                                )
                            }
                          else  if ( keyboardType == KeyboardType.SEMIPURENUMBERS) {
                                KeyboardKey(
                                    keyboardType = keyboardType,
                                    key = key,
                                    horPadding = horPadding,
                                    vertPadding = vertPadding,
                                    modifier = Modifier
                                        .weight(calculateWeights3(row)[index])
                                        .height(keyHeight)
                                        .width(keyWidth),
                                    onKeyboardTypeChange = { newType ->
                                        keyboardType = newType
                                    }
                                )
                            }

                            else {
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
    val isSpecial = key in listOf(
        SPECIAL_LANGUAGE,
        SPECIAL_ENTER,
        SPECIAL_EMOJI,
        SPECIAL_ARROW_TOP,
        SPECIAL_DASH,
        SPECIAL_DOUBLE_DASH,
        SPECIAL_BACK,
        SPECIAL_ARROW_RIGHT
    )
    val textSize = KeyboardSizing.calculateTextSize(
        config.screenWidthDp.dp,
        config.screenHeightDp.dp,
        key.length > 5 || key == SPECIAL_NUMKEY
    )
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 50)
    )
    if (key == " ") {
        Spacer(modifier = modifier)
        return
    }
    Box(
        modifier = modifier
            .scale(scale)
            .pointerInput(keyboardType) {
                detectTapGestures(
                    onPress = { event ->
                        isPressed = true
                        showPopup = true
                        short_vibrate(ctx)
                        val longPressJob = coroutineScope.launch {
                            delay(300L)  // Reduced from 400ms for faster response
                            if (isPressed) {
                                isLongPressActive = true
                                when (key) {
                                    in listOf(
                                        SPECIAL_BACK,
                                        SPECIAL_ENTER,
                                        SPECIAL_LANGUAGE
                                    ) -> startContinuousAction(
                                        ctx,
                                        key
                                    )

                                    else -> handleLongPress(ctx, key)
                                }
                            }
                        }

                        tryAwaitRelease()
                        longPressJob.cancel()

                        if (isLongPressActive) {
                            stopContinuousAction()
                            isLongPressActive = false
                        } else {
                            handleShortKeyAction(keyboardType, ctx, key, onKeyboardTypeChange)
                        }

                        // Reset states
                        isPressed = false
                        showPopup = false
                    }
                )
            }
            .padding(horizontal = horPadding, vertical = vertPadding)
            .clip(RoundedCornerShape(KeyboardSizing.keyCornerRadius))
            .background(
                when {
                    isPressed -> Color.Gray.copy(alpha = 0.5f) // Change color when pressed
                    key == SPECIAL_ARROW_TOP && (ctx.isCapsEnabled.value || ctx.isNextLetterCaps.value) -> getKeyPopupTextColor()
                    else -> getKeyColor(isSpecial)
                }
            )
            .border(1.dp, getBorderColor(), RoundedCornerShape(KeyboardSizing.keyCornerRadius)),
        contentAlignment = Alignment.Center
    ) {
        when {
            key.contains("^") -> DualTextKey(key, config.screenWidthDp.dp, config.screenHeightDp.dp)
            isSpecial && key != SPECIAL_LANGUAGE -> IconKey(key)
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

                        key.contains(SPECIAL_HASH_STAR) -> Text(
                            text = if (isLongPressActive) "#" else "*",
                            fontSize = if (isLongPressActive) 18.sp else 18.sp,
                            color = getKeyTextColor(isLongPressActive),
                            fontWeight = FontWeight.Bold
                        )

                        key.contains(SPECIAL_ZERO_PLUSH) -> Text(
                            text = if (isLongPressActive) "0" else "+",
                            fontSize = if (isLongPressActive) 18.sp else 18.sp,
                            color = getKeyTextColor(isLongPressActive),
                            fontWeight = FontWeight.Bold
                        )

                        isSpecial && key != SPECIAL_LANGUAGE -> IconKey(key)
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
private fun calculateWeights3(row: List<String>): List<Float> {
    val specialWeights =
        mapOf(
            SPECIAL_BACK to 1.5f,
            SPECIAL_DOUBLE_DASH to 1.5f,
            SPECIAL_ENTER to 1.5f,
            SPECIAL_ZERO_PLUSH to 2f,
            SPECIAL_ABCKEY to 1.5f,
            SPECIAL_QONEHASH to 1.5f,
            "=" to 1.5f,
            "+" to 1.5f,
            "%" to 1.5f,
            " " to 0.5f,
            "(" to 1.5f,
            ")" to 1.5f,

        )
    val specialWeightSum = row.sumOf { specialWeights[it]?.toDouble() ?: 0.0 }.toFloat()
    val normalWeight = (10f - specialWeightSum) / (row.count { it !in specialWeights })
    return row.map { specialWeights[it] ?: normalWeight }
}
private fun calculateWeights2(row: List<String>): List<Float> {
    val specialWeights =
        mapOf(
            SPECIAL_LANGUAGE to 3.5f,
        )
    val specialWeightSum = row.sumOf { specialWeights[it]?.toDouble() ?: 0.0 }.toFloat()
    val normalWeight = (10f - specialWeightSum) / (row.count { it !in specialWeights })
    return row.map { specialWeights[it] ?: normalWeight }
}

private fun calculateWeights(row: List<String>): List<Float> {
    val specialWeights =
        mapOf(
            SPECIAL_LANGUAGE to 3.5f,
            SPECIAL_QONEHASH to 1.5f,
            SPECIAL_CHANGESPE to 1.5f,
            SPECIAL_ABCKEY to 1.5f,
            SPECIAL_QNUMKEY to 1.5f,
            " " to 0.5f,
            SPECIAL_ENTER to 2f,
            SPECIAL_ARROW_TOP to 1.5f,
            SPECIAL_BACK to 1.5f
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
        SPECIAL_BACK -> ctx.currentInputConnection?.deleteSurroundingText(1, 0)
        SPECIAL_ENTER -> ctx.currentInputConnection?.sendKeyEvent(
            KeyEvent(
                KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_ENTER
            )
        )

        SPECIAL_EMOJI -> onKeyboardTypeChange(KeyboardType.EMOJI)
        SPECIAL_ARROW_TOP -> ctx.toggleCaps()
        SPECIAL_ARROW_RIGHT -> ctx.currentInputConnection.performEditorAction(EditorInfo.IME_ACTION_NEXT)
        SPECIAL_LANGUAGE -> ctx.currentInputConnection?.commitText(" ", 1)
        SPECIAL_QONEHASH -> {
            onKeyboardTypeChange(KeyboardType.NUMBERS)
        }


        SPECIAL_CHANGESPE -> {
            onKeyboardTypeChange(KeyboardType.NUMBERS2)
        }

        SPECIAL_QNUMKEY -> {
            onKeyboardTypeChange(KeyboardType.NUMBERS)
        }

        SPECIAL_ABCKEY -> {
            onKeyboardTypeChange(KeyboardType.LETTERS)
        }

        SPECIAL_NUMKEY -> {
            onKeyboardTypeChange(KeyboardType.SEMIPURENUMBERS)
        }

        else -> {
            if (key.contains(SPECIAL_HASH_STAR)) {
                val text = "*"
                ctx.currentInputConnection?.commitText(text, text.length)
            } else if (key.contains(SPECIAL_DOUBLE_DASH)) {
                ctx.currentInputConnection?.commitText(" ", 1)
            } else if (key.contains(SPECIAL_ZERO_PLUSH)) {
                val text = "0"
                ctx.currentInputConnection?.commitText(text, text.length)
            } else if (keyboardType == KeyboardType.NUMBERS || keyboardType == KeyboardType.NUMBERS2) {
                ctx.currentInputConnection?.commitText(key, key.length)
            } else {
                val text = if (key.contains("^")) key.split("^")[0] else key
                ctx.currentInputConnection?.commitText(
                    if (ctx.isCapsEnabled.value || ctx.isNextLetterCaps.value) text.uppercase() else text.lowercase(),
                    text.length
                )
                if (!key.contains(SPECIAL_ARROW_TOP)) {
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
                SPECIAL_BACK -> R.drawable.delete_dark
                SPECIAL_ENTER -> R.drawable.enter_dark
                SPECIAL_EMOJI -> R.drawable.emoji_dark
                SPECIAL_ARROW_TOP -> R.drawable.arrow_top_dark
                SPECIAL_DASH -> R.drawable.dash
                SPECIAL_DOUBLE_DASH -> R.drawable.space_bar
                SPECIAL_ARROW_RIGHT -> R.drawable.arrow_right
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
    if (key.contains(SPECIAL_HASH_STAR)) {
        val text = "#"
        ctx.currentInputConnection?.commitText(text, text.length)
    }
    if (key.contains(SPECIAL_ZERO_PLUSH)) {
        val text = "+"
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
                SPECIAL_BACK -> ctx.currentInputConnection?.deleteSurroundingText(1, 0)
                SPECIAL_ENTER -> ctx.currentInputConnection?.sendKeyEvent(
                    KeyEvent(
                        KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_ENTER
                    )
                )

                SPECIAL_LANGUAGE -> ctx.currentInputConnection?.commitText(" ", 1)
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
