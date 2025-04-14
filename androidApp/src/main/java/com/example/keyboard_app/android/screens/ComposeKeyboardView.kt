package com.example.keyboard_app.android.screens
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalContext
import com.example.keyboard_app.android.KeyboardService

class ComposeKeyboardView(context: Context) : AbstractComposeView(context) {
    @Composable
    override fun Content() {
        val keyboardService = LocalContext.current as? KeyboardService
        LaunchedEffect(keyboardService?.lifecycle) {
            keyboardService?.resetKeyboardState()
        }
        KeyboardScreen()
    }
}