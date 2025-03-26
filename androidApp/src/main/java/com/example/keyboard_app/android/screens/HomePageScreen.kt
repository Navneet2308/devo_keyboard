package com.example.keyboard_app.android.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun HomePageScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF918BFF))  // Light purple background
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
    }
}


