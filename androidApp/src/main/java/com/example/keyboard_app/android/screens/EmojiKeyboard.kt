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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyboard_app.android.KeyboardService
import com.example.keyboard_app.android.R
import com.example.keyboard_app.android.ime.KeyboardType
import com.example.keyboard_app.android.model.EmojiCategories
import com.example.keyboard_app.android.model.EmojiCategory
import com.example.keyboard_app.android.utils.getBorderColor
import com.example.keyboard_app.android.utils.getKeyColor
import com.example.keyboard_app.android.utils.getKeyIconColor
import com.example.keyboard_app.android.utils.getKeyTextColor
import org.json.JSONObject
import java.io.InputStreamReader

@Composable
fun EmojiKeyboard(service: KeyboardService, onKeyboardTypeChange: (KeyboardType) -> Unit = {}) {
    val context = LocalContext.current
    var emojiCategories by remember { mutableStateOf<List<EmojiCategory>>(emptyList()) }
    var selectedCategory by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    // Load emoji data from JSON file
    LaunchedEffect(Unit) {
        val inputStream = context.assets.open("emoji_categories.json")
        val jsonString = InputStreamReader(inputStream).use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        val categoriesArray = jsonObject.getJSONArray("categories")
        
        val categories = mutableListOf<EmojiCategory>()
        for (i in 0 until categoriesArray.length()) {
            val categoryObj = categoriesArray.getJSONObject(i)
            val icon = categoryObj.getString("icon")
            val emojisArray = categoryObj.getJSONArray("emojis")
            val emojis = mutableListOf<String>()
            
            for (j in 0 until emojisArray.length()) {
                emojis.add(emojisArray.getString(j))
            }
            
            categories.add(EmojiCategory(icon, emojis))
        }
        
        emojiCategories = categories
    }

    // Optimize filtered emojis calculation
    val filteredEmojis by remember(selectedCategory, searchQuery, emojiCategories) {
        derivedStateOf {
            if (emojiCategories.isEmpty()) return@derivedStateOf emptyList()
            
            if (searchQuery.isEmpty()) {
                emojiCategories[selectedCategory].emojis
            } else {
                emojiCategories.flatMap { it.emojis }
                    .filter { it.contains(searchQuery, ignoreCase = true) }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        // Category selector
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            state = rememberLazyListState()
        ) {
            itemsIndexed(emojiCategories) { index, category ->
                CategoryIcon(
                    iconRes = getIconResourceId(category.icon),
                    isSelected = selectedCategory == index,
                    onClick = { selectedCategory = index; searchQuery = "" }
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
                    onClick = { service.currentInputConnection?.commitText(emoji, emoji.length) }
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
            fontSize = 24.sp,
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