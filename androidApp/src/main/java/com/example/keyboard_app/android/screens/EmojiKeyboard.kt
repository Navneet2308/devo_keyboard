package com.example.keyboard_app.android.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyboard_app.android.DevooApplication
import com.example.keyboard_app.android.KeyboardService
import com.example.keyboard_app.android.R
import com.example.keyboard_app.android.common.KeyboardSizing
import com.example.keyboard_app.android.ime.KeyboardType
import com.example.keyboard_app.android.utils.getBorderColor
import com.example.keyboard_app.android.utils.getKeyColor
import com.example.keyboard_app.android.utils.getKeyIconColor
import com.example.keyboard_app.android.utils.getKeyTextColor


@Composable
fun EmojiKeyboard(service: KeyboardService, onKeyboardTypeChange: (KeyboardType) -> Unit = {}) {
    var selectedCategory by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    val emojiCategories = remember { DevooApplication.emojiCategories }
    val currentEmojis = remember { mutableStateListOf<String>() }


    LaunchedEffect(selectedCategory) {
        searchQuery = ""
        currentEmojis.clear()
        if (emojiCategories.isNotEmpty()) {
            currentEmojis.addAll(emojiCategories[selectedCategory].emojis)
        }
    }

    // Filter emojis based on search query
    val filteredEmojis by remember(searchQuery, currentEmojis) {
        derivedStateOf {
            if (searchQuery.isEmpty()) currentEmojis
            else currentEmojis.filter { it.contains(searchQuery, ignoreCase = true) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            state = rememberLazyListState()
        ) {
            itemsIndexed(emojiCategories) { index, category ->
                val iconRes = getIconResourceId(category.icon)
                CategoryIcon(
                    iconRes = iconRes,
                    isSelected = selectedCategory == index,
                    onClick = { selectedCategory = index }
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(8),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(4.dp),
            state = rememberLazyGridState(),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(filteredEmojis, key = { it }) { emoji ->
                EmojiItem(
                    emoji = emoji,
                    onClick = {
                        service.currentInputConnection?.commitText(emoji, emoji.length)
                    }
                )
            }
        }

        BottomControls(service, onKeyboardTypeChange)
    }
}

private fun getIconResourceId(iconName: String): Int {
    return when (iconName) {
        "emoji_dark" -> R.drawable.emoji_dark
        "heart_dark" -> R.drawable.heart_dark
        "thumb_dark" -> R.drawable.thumb_dark
        "cat_dark" -> R.drawable.cat_dark
        "food_dark" -> R.drawable.food_dark
        "sport_dark" -> R.drawable.sport_dark
        "car_dark" -> R.drawable.car_dark
        "bulb" -> R.drawable.bulb
        "flash_dark" -> R.drawable.flash_dark
        "flag_dark" -> R.drawable.flag_dark
        else -> R.drawable.emoji_dark
    }
}

@Composable
private fun CategoryIcon(iconRes: Int, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) getKeyColor(true) else getKeyColor(false))
            .border(1.dp, getBorderColor(), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = getKeyIconColor(),
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun EmojiItem(emoji: String, onClick: () -> Unit) {
    val config = LocalConfiguration.current
    val textSize = KeyboardSizing.calculateTextSize(
        config.screenWidthDp.dp,
        config.screenHeightDp.dp,
        false
    )
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(getKeyColor(false))
            .border(1.dp, getBorderColor(), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = textSize,
            color = getKeyTextColor()
        )
    }
}

@Composable
private fun BottomControls(service: KeyboardService, onKeyboardTypeChange: (KeyboardType) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(R.drawable.keyboard_dark),
            contentDescription = null,
            tint = getKeyIconColor(),
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    onKeyboardTypeChange(KeyboardType.LETTERS)
                }
        )
        Icon(
            painter = painterResource(R.drawable.delete_dark),
            contentDescription = null,
            tint = getKeyIconColor(),
            modifier = Modifier
                .size(30.dp)
                .clickable { service.currentInputConnection?.deleteSurroundingText(1, 0) }
        )
    }
}