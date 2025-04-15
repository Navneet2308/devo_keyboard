package com.example.keyboard_app.android.model

data class EmojiCategory(
    val icon: String,
    val emojis: List<String>
)

data class EmojiCategories(
    val categories: List<EmojiCategory>
) 