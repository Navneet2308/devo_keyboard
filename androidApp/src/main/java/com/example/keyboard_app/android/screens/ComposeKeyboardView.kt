package com.example.keyboard_app.android.screens
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView

class ComposeKeyboardView(
    context: Context,
    private val getKeys: () -> List<List<String>>
) : AbstractComposeView(context) {
    @Composable
    override fun Content() {
        KeyboardScreen(getKeys)
    }
}