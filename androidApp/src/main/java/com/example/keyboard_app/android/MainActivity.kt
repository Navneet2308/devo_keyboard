package com.example.keyboard_app.android

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.keyboard_app.android.screens.HomePageScreen
import com.example.keyboard_app.android.screens.KeyboardSetupScreen
import com.example.keyboard_app.android.theming.DevooTheme
import com.goodwy.keyboard.lib.util.InputMethodUtils

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DevooTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary
                ) {
                    StartScreen(this)
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @Composable
    fun StartScreen(context: Context) {
        val isKeyBoardEnabled = InputMethodUtils.isKeyboardEnabled(context)
        val isKeyBoardSelected = InputMethodUtils.isKeyboardSelected(context)
        if (isKeyBoardEnabled && isKeyBoardSelected) {
            HomePageScreen()
        } else {
            KeyboardSetupScreen(context)
        }
    }
}






