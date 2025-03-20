package com.example.keyboard_app.android

import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.example.keyboard_app.KeyboardLogic


class KeyboardService : InputMethodService() {

    private val keyboardManager = KeyboardLogic()

    override fun onCreateInputView(): View {
        val layout = layoutInflater.inflate(R.layout.keyboard_layout, null)
        val rowKeys = "1234567890QWERTYUIOPASDFGHJKLZXCVBNM".map { it.toString() }

        rowKeys.forEach { key ->
            layout.findViewById<Button>(
                resources.getIdentifier("btn_${key.lowercase()}", "id", packageName)
            )?.apply {
                setOnClickListener {
                    val processedKey = keyboardManager.processKey(key)
                    currentInputConnection.commitText(processedKey, 1)
                }
            }
        }

        layout.findViewById<ImageButton>(R.id.bnt_back)?.setOnClickListener {
            val (before, after) = keyboardManager.deleteCharacter()
            currentInputConnection.deleteSurroundingText(before, after)
        }

        layout.findViewById<Button>(R.id.btn_language)?.setOnClickListener {
            val space = keyboardManager.insertSpace()
            currentInputConnection.commitText(space, 1)
        }

        layout.findViewById<ImageButton>(R.id.bnt_caps)?.setOnClickListener {
            val isCaps = keyboardManager.toggleCase()
            rowKeys.forEach { key ->
                layout.findViewById<Button>(
                    resources.getIdentifier("btn_${key.lowercase()}", "id", packageName)
                )?.text = if (isCaps) key else key.lowercase()
            }
        }



        return layout
    }
}
