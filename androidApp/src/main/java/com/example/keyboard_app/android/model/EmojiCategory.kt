package com.example.keyboard_app.android.model

import kotlinx.serialization.Serializable


@Serializable
data class EmojiCategory(
    val icon: String,
    val emojis: List<String>
)

@Serializable
data class EmojiData(
    val emojiCategories: List<EmojiCategory>
)
