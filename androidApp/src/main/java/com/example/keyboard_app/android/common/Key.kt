package com.example.keyboard_app.android.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
    fun Key(modifier: Modifier = Modifier, label: String, onClick: () -> Unit) {
        val shape = RoundedCornerShape(4.dp)
        //TODO: make clickable outside but don't show ripple
        Box(modifier = modifier
            .padding(2.dp)
            .clip(shape)
            .clickable(onClick = onClick)
            .background(Color.White)
            .padding(vertical = 12.dp, horizontal = 4.dp), contentAlignment = Alignment.Center) {
            Text(text = label, fontSize = 20.sp)
        }
    }
@Composable
fun KeyRow(keys: List<String>) {
    Row(modifier = Modifier.fillMaxWidth().background(color = Color.Gray)) {
        keys.forEach {
            Key(modifier = Modifier.weight(1f), label = it, onClick = {  })
        }
    }
}