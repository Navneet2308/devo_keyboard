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
import com.example.keyboard_app.android.R


object Key {
    fun getEmojiKeyboard(): List<Pair<Int, List<String>>> {
        return listOf(
            R.drawable.emoji_dark to listOf(
                "😀", "😃", "😄", "😁", "😅", "😂", "🤣", "😊", "😇", "🙂", "🙃", "😉", "😌", "😍", "🥰", "😘",
                "😗", "😙", "😚", "😋", "😛", "😝", "😜", "🤪", "🤨", "🧐", "🤓", "😎", "🤩", "🥳", "😏", "😒",
                "😞", "😔", "😟", "😕", "🙁", "☹️", "😣", "😖", "😫", "😩", "🥺", "😢", "😭", "😤", "😠", "😡"
            ),
            R.drawable.heart_dark to listOf(
                "❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍", "🤎", "💔", "❣️", "💕", "💞", "💓", "💗", "💖",
                "💘", "💝", "💟", "☮️", "✝️", "☪️", "🕉️", "☸️", "✡️", "🔯", "🕎", "☯️", "☦️", "🛐", "⛎", "♈",
                "♉", "♊", "♋", "♌", "♍", "♎", "♏", "♐", "♑", "♒", "♓", "🆔", "⚛️", "🉑", "☢️", "☣️", "📴"
            ),
            R.drawable.thumb_dark to listOf(
                "👍", "👎", "👌", "✌️", "🤞", "🤝", "👏", "🙌", "👋", "🤚", "🖐️", "✋", "🖖", "👊", "🤛", "🤜",
                "🤞", "✌️", "🤟", "🤘", "🤙", "👈", "👉", "👆", "🖕", "👇", "☝️", "👍", "👎", "👊", "✊", "🤛",
                "🤜", "🤞", "✌️", "🤟", "🤘", "🤙", "👈", "👉", "👆", "🖕", "👇", "☝️", "👍", "👎", "👊", "✊"
            ),
            R.drawable.cat_dark to listOf(
                "🐱", "🐶", "🐭", "🐹", "🐰", "🦊", "🐻", "🐼", "🐨", "🐯", "🦁", "🐮", "🐷", "🐸", "🐵", "🐔",
                "🐧", "🐦", "🐤", "🐣", "🐥", "🦆", "🦅", "🦉", "🦇", "🐺", "🐗", "🐴", "🦄", "🐝", "🐛", "🦋",
                "🐌", "🐚", "🐞", "🐜", "🦗", "🕷️", "🕸️", "🦂", "🦟", "🦠", "🐢", "🐙", "🦑", "🦐", "🦞", "🦀"
            ),
            R.drawable.food_dark to listOf(
                "🍔", "🍕", "🌭", "🌮", "🌯", "🥙", "🍖", "🍗", "🥩", "🥓", "🍜", "🍝", "🍠", "🍤", "🍣", "🍱",
                "🥟", "🍱", "🍘", "🍙", "🍚", "🍛", "🍜", "🍝", "🍠", "🍢", "🍣", "🍤", "🍥", "🥮", "🍡", "🥟",
                "🥠", "🥡", "🍦", "🍧", "🍨", "🍩", "🍪", "🎂", "🍰", "🧁", "🥧", "🍫", "🍬", "🍭", "🍮", "🍯"
            ),
            R.drawable.sport_dark to listOf(
                "⚽", "🏀", "🏈", "⚾", "🥎", "🎾", "🏐", "🏉", "🥅", "🏸", "🏓", "🏒", "🏑", "🏏", "🥅", "🎯",
                "🏈", "⚾", "🥎", "🎾", "🏐", "🏉", "🥅", "🏸", "🏓", "🏒", "🏑", "🏏", "🥅", "🎯", "🏈", "⚾",
                "🥎", "🎾", "🏐", "🏉", "🥅", "🏸", "🏓", "🏒", "🏑", "🏏", "🥅", "🎯", "🏈", "⚾", "🥎", "🎾"
            ),
            R.drawable.car_dark to listOf(
                "🚗", "🚕", "🚙", "🚌", "🚎", "🏎️", "🚓", "🚑", "🚒", "🚐", "🚚", "🚛", "🚜", "🦯", "🦽", "🦼",
                "🚲", "🛵", "🛺", "🚨", "🚔", "🚍", "🚘", "🚖", "🚡", "🚠", "🚟", "🚃", "🚋", "🚞", "🚝", "🚄",
                "🚅", "🚈", "🚂", "🚆", "🚇", "🚊", "🚉", "✈️", "🛩️", "🛫", "🛬", "🚀", "🛸", "🚁", "🛟", "⛵"
            ),
            R.drawable.bulb to listOf(
                "💡", "🔦", "🏮", "🪔", "📱", "📲", "💻", "⌚", "🕰️", "⏰", "⏱️", "📷", "📸", "📹", "🎥", "📽️",
                "🎞️", "📞", "☎️", "📟", "📠", "📺", "📻", "🎙️", "🎚️", "🎛️", "🧭", "⏱️", "⏲️", "⏰", "🕰️", "⌚",
                "📱", "📲", "💻", "⌨️", "🖥️", "🖨️", "🖱️", "🖲️", "🕹️", "🗜️", "💽", "💾", "💿", "📀", "📼"
            ),
            R.drawable.flash_dark to listOf(
                "⚡", "💫", "✨", "⭐", "🌟", "💥", "💢", "💦", "💨", "🕳️", "💣", "💬", "👁️", "👁️‍🗨️", "🗨️", "🗯️",
                "💭", "💤", "🗯️", "💭", "💤", "🗯️", "💭", "💤", "🗯️", "💭", "💤", "🗯️", "💭", "💤", "🗯️", "💭",
                "💤", "🗯️", "💭", "💤", "🗯️", "💭", "💤", "🗯️", "💭", "💤", "🗯️", "💭", "💤", "🗯️", "💭", "💤"
            ),
            R.drawable.flag_dark to listOf(
                "🏁",
                "🚩",
                "🏳️",
                "🏳️‍🌈",
                "🏳️‍⚧️",
                "🏴‍☠️",
                "🏳️‍⚧️",
                "🏳️‍🌈",
                "🏳️‍⚧️",
                "🏴‍☠️",
                "🏳️‍🌈",
                "🏳️‍⚧️",
                "🏴‍☠️",
                "🏳️‍🌈",
                "🏳️‍⚧️",
                "🏴‍☠️",
                "🏳️‍🌈",
                "🏳️‍⚧️",
                "🏴‍☠️",
                "🏳️‍🌈",
                "🏳️‍⚧️",
                "🏴‍☠️",
                "🏳️‍🌈",
                "🏳️‍⚧️",
                "🏴‍☠️",
                "🏳️‍🌈",
                "🏳️‍⚧️",
                "🏴‍☠️",
                "🏳️‍🌈",
                "🏳️‍⚧️",
                "🏴‍☠️",
                "🏳️‍🌈",
                "🏳️‍⚧️",
                "🏴‍☠️",
                "🏳️‍🌈",
                "🏳️‍⚧️",
                "🏴‍☠️",
                "🏳️‍🌈",
                "🏳️‍⚧️",
                "🏴‍☠️",
                "🏳️‍🌈",
                "🏳️‍⚧️",
                "🏴‍☠️",
                "🏳️‍🌈",
                "🏳️‍⚧️",
                "🏴‍☠️"
            )
        )
    }

     fun getNumberKeys(): List<List<String>> {
        return listOf(
            listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
            listOf(" ", "@", "#", "$", "%", "&", "-", "+", "(", ")", " "),
            listOf("↑", "*", "'", ":", ";", "!", "?", "←"),
            listOf("?1#", ":)", ",", "language", ".", "⏎")
        )
    }

     fun getAlphabetKeys(isCapsEnabled:Boolean): List<List<String>> {
        return if (isCapsEnabled) {
            listOf(
                listOf("Q^1", "W^2", "E^3", "R^4", "T^5", "Y^6", "U^7", "I^8", "O^9", "P^0"),
                listOf(" ", "A^@", "S^#", "D^$", "F^%", "G^&", "H^-", "J^+", "K^(", "L^)", " "),
                listOf("↑", "Z^*", "X^", "C^'", "V^:", "B^;", "N^!", "M^?", "←"),
                listOf("?1#", ":)", ",", "language", ".", "⏎")
            )
        } else {
            listOf(
                listOf("q^1", "w^2", "e^3", "r^4", "t^5", "y^6", "u^7", "i^8", "o^9", "p^0"),
                listOf(" ", "a^@", "s^#", "d^$", "f^%", "g^&", "h^-", "j^+", "k^(", "l^)", " "),
                listOf("↑", "z^*", "x^", "c^'", "v^:", "b^;", "n^!", "m^?", "←"),
                listOf("?1#", ":)", ",", "language", ".", "⏎")
            )
        }
    }

}