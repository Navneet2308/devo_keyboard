package com.example.keyboard_app.android.screens

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
private val handler = Handler(Looper.getMainLooper())
private var isLongPressed = false
private var isClicked = false

@Composable
fun KeyboardScreen(getKeys: () -> List<List<String>>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(getKeyboardBG())
            .padding(8.dp)
    ) {
        getKeys().forEach { row ->
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

fun calculateWeights(row: List<String>, totalWeight: Float = 10f): List<Float> {
    val specialWeights = mapOf("language" to 4f, " " to 0.5f, "⏎" to 2f, "↑" to 1.5f, "←" to 1.5f)
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
    val isSpecial = key in listOf("←", ":)", "?1#", "language", "⏎")
    val isSmallText = key.length > 3 || key == "English (United Sta..."

    if (key.contains(" ")) {
        Spacer(modifier = modifier)
    } else {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(getKeyColor(isSpecial))
                .pointerInteropFilter { event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            handleKeyAction(ctx, key)
                            isClicked = true
                            startLongPressAction(ctx, key)
                            true
                        }
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            stopLongPressAction()
                            isClicked = false
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
                key in listOf(":)", "↑", "←", "⏎") -> IconKey(key, isSmallText)
                else -> TextKey(key, isSmallText)
            }
        }
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
            color =getKeyTextColor(), modifier = Modifier.align(Alignment.Center)
        )
    }
}
fun stopLongPressAction() {
    isLongPressed = false
    handler.removeCallbacksAndMessages(null)
}

@Composable
fun IconKey(key: String, isSmallText: Boolean) {
    val icon = when (key) {
        ":)" -> R.drawable.emoji_dark
        "↑" -> R.drawable.arrow_top_dark
        "←" -> R.drawable.delete_dark
        "⏎" -> R.drawable.enter_dark
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
fun startLongPressAction(ctx: Context, key: String) {
    isLongPressed = true
    val repeatAction = object : Runnable {
        override fun run() {
            if (isLongPressed) {
                handleLongKeyAction(ctx, key)
                handler.postDelayed(this, 100)  // Adjust the delay as needed (100ms)
            }
        }
    }
    handler.post(repeatAction)
}

fun handleLongKeyAction(ctx: Context, key: String) {
    val service = ctx as? KeyboardService ?: return
    val cleanedKey = key.first().toString()
    when (cleanedKey) {
        "⏎" -> service.currentInputConnection?.sendKeyEvent(
            KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER)
        )

        "←" -> service.currentInputConnection?.deleteSurroundingText(1, 0)

    }
}


fun handleKeyAction(ctx: Context, key: String) {
    val service = ctx as? KeyboardService ?: return
    val cleanedKey = key.first().toString()
    when (cleanedKey) {
        "language" -> service.currentInputConnection?.commitText(" ", 1)
        "⏎" -> {}
        "←" -> {}
        "↑" -> service.toggleCaps()
        else -> {
            val textToCommit =
                if (service.isCapsEnabled) cleanedKey.uppercase() else cleanedKey.lowercase()
            service.currentInputConnection?.commitText(textToCommit, textToCommit.length)
        }
    }
}
