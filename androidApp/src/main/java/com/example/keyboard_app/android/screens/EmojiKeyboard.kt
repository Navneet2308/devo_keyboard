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
import com.example.keyboard_app.android.model.EmojiData
import com.example.keyboard_app.android.utils.getBorderColor
import com.example.keyboard_app.android.utils.getKeyColor
import com.example.keyboard_app.android.utils.getKeyIconColor
import com.example.keyboard_app.android.utils.getKeyTextColor
import kotlinx.serialization.json.Json

@Composable
fun EmojiKeyboard(service: KeyboardService, onKeyboardTypeChange: (KeyboardType) -> Unit = {}
) {
    val emojiCategories = remember {
        listOf(
            R.drawable.emoji_dark to listOf(
                "😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣", "🥲", "😊", "😇", "🙂", "🙃", "😉",
                "😌", "😍", "🥰", "😘", "😗", "😙", "😚", "😋", "😛", "😝", "😜", "🤪", "🤨", "🧐",
                "🤓", "😎", "🥸", "🤩", "🥳", "😏", "😒", "😞", "😔", "😟", "😕", "🙁", "☹️", "😣",
                "😖", "😫", "😩", "🥺", "😢", "😭", "😤", "😠", "😡"
            ),
            R.drawable.heart_dark to listOf(
                "❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍", "🤎", "💔", "❣️", "💕", "💞", "💓", "💗",
                "💖", "💘", "💝", "💟", "💌", "📩", "📬", "📭", "📫", "📪", "📤", "📥", "📦", "📧", "📴"
            ),
            R.drawable.thumb_dark to listOf(
                "👍", "👎", "👊", "✊", "🤛", "🤜", "👏", "🙌", "👐", "🤲", "🙏", "✍️", "💅", "🤝", "👋",
                "🤚", "🖐️", "✋", "🖖", "👌", "🤌", "🤏", "✌️", "🤞", "🫰", "🤟", "🤘", "🤙"
            ),
            R.drawable.cat_dark to listOf(
                "🐱", "🐶", "🐭", "🐹", "🐰", "🦊", "🐻", "🐼", "🐻‍❄️", "🐨", "🐯", "🦁", "🐮", "🐷", "🐸",
                "🐵", "🙈", "🙉", "🙊", "🐒", "🐔", "🐧", "🐦", "🐤", "🐣", "🦆", "🦅", "🦉", "🦇", "🦀"
            ),
            R.drawable.food_dark to listOf(
                "🍔", "🍕", "🌭", "🍟", "🥪", "🌮", "🌯", "🥙", "🧆", "🥗", "🥘", "🍲", "🍛", "🍜", "🍝",
                "🍤", "🍣", "🍱", "🥟", "🦪", "🍙", "🍚", "🍘", "🍥", "🥮", "🍢", "🍡", "🍧", "🍨", "🍯"
            ),
            R.drawable.sport_dark to listOf(
                "⚽", "🏀", "🏈", "⚾", "🥎", "🎾", "🏐", "🏉", "🥏", "🎱", "🪀", "🏓", "🏸", "🥅", "🏒",
                "🏑", "🥍", "🏏", "🪃", "🥊", "🥋", "🎽", "🛹", "🛷", "⛷️", "🏂", "🏋️‍♂️", "🚴‍♀️", "⛹️‍♀️"
            ),
            R.drawable.car_dark to listOf(
                "🚗", "🚕", "🚙", "🚌", "🚎", "🏎️", "🚓", "🚑", "🚒", "🚐", "🛻", "🚚", "🚛", "🚜", "🛺",
                "🚲", "🛴", "🏍️", "🛵", "🛶", "⛵", "🛳️", "🚤", "🛸", "🚀", "✈️", "🛩️", "🪂", "🚁"
            ),
            R.drawable.bulb to listOf(
                "💡", "🔦", "🕯️", "💻", "🖥️", "🖨️", "📷", "📸", "📹", "🎥", "📞", "☎️", "📟", "📠", "📺",
                "📻", "🎙️", "🎚️", "🎛️", "🧭", "🧱", "📼"
            ),
            R.drawable.flash_dark to listOf(
                "⚡", "💫", "✨", "🌟", "🔥", "🌪️", "🌈", "☄️", "💥", "💣", "🧨", "🌋", "🌀", "💤"
            ),
            R.drawable.flag_dark to listOf(
                "🏁", "🚩", "🎌", "🏴", "🏳️", "🏳️‍🌈", "🏳️‍⚧️", "🏴‍☠️", "🇮🇳", "🇺🇸", "🇬🇧", "🇨🇳", "🇯🇵", "🇰🇷",
                "🇫🇷", "🇩🇪", "🇨🇦", "🇧🇷", "🇮🇹", "🇪🇸", "🇷🇺"
            )
        )
    }


    var selectedCategory by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    // Optimize filtered emojis calculation
    val filteredEmojis by remember(selectedCategory, searchQuery) {
        derivedStateOf {
            if (searchQuery.isEmpty()) {
                emojiCategories[selectedCategory].second
            } else {
                emojiCategories.flatMap { it.second }
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
            state = rememberLazyListState() // Add state for better performance
        ) {
            itemsIndexed(emojiCategories) { index, (icon, _) ->
                CategoryIcon(
                    iconRes = icon,
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
        BottomControls(service,onKeyboardTypeChange)
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
private fun BottomControls(service: KeyboardService,onKeyboardTypeChange: (KeyboardType) -> Unit) {
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