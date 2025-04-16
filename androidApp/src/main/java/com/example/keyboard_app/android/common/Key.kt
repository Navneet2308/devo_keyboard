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
import com.example.keyboard_app.android.ime.KeyboardType


object Key {

    val SPECIAL_ABCKEY = "ABC"
    val SPECIAL_NUMKEY = "1234"
    val SPECIAL_LANGUAGE = "language"
    val SPECIAL_ENTER = "⏎"
    val SPECIAL_BACK = "←"
    val SPECIAL_CHANGESPE = "=\\\\<"
    val SPECIAL_QNUMKEY = "?123"


    val SPECIAL_DASH = "—"
    val SPECIAL_DOUBLE_DASH = "--"
    val SPECIAL_ARROW_RIGHT = "→"
    val SPECIAL_HASH_STAR = "* #"
    val SPECIAL_ZERO_PLUSH = "0  +"

    val SPECIAL_QONEHASH = "?1#"
    val SPECIAL_EMOJI = ":)"
    val SPECIAL_ARROW_TOP = "↑"


    fun getAlphabetKeys(isFirstCapsEnabled: Boolean, isCapsEnabled: Boolean): List<List<String>> {
        return if (isCapsEnabled || isFirstCapsEnabled) {
            listOf(
                listOf("Q^1", "W^2", "E^3", "R^4", "T^5", "Y^6", "U^7", "I^8", "O^9", "P^0"),
                listOf(" ", "A^@", "S^#", "D^$", "F^%", "G^&", "H^-", "J^+", "K^(", "L^)", " "),
                listOf(
                    SPECIAL_ARROW_TOP,
                    "Z^*",
                    "X^",
                    "C^'",
                    "V^:",
                    "B^;",
                    "N^!",
                    "M^?",
                    SPECIAL_BACK
                ),
                listOf(SPECIAL_QONEHASH, SPECIAL_EMOJI, ",", SPECIAL_LANGUAGE, ".", SPECIAL_ENTER)
            )
        } else {
            listOf(
                listOf("q^1", "w^2", "e^3", "r^4", "t^5", "y^6", "u^7", "i^8", "o^9", "p^0"),
                listOf(" ", "a^@", "s^#", "d^$", "f^%", "g^&", "h^-", "j^+", "k^(", "l^)", " "),
                listOf(SPECIAL_ARROW_TOP, "z^*", "x^", "c^'", "v^:", "b^;", "n^!", "m^?", SPECIAL_BACK),
                listOf(SPECIAL_QONEHASH, SPECIAL_EMOJI, ",", SPECIAL_LANGUAGE, ".", SPECIAL_ENTER)
            )
        }
    }


    fun getNumberKeys(): List<List<String>> {
        return listOf(
            listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
            listOf(" ", "@", "#", "$", "%", "&", "-", "+", "(", ")", " "),
            listOf(SPECIAL_CHANGESPE, "*","''" ,"'", ":", ";", "!", "?", SPECIAL_BACK),
            listOf(SPECIAL_ABCKEY, SPECIAL_NUMKEY, ",", SPECIAL_LANGUAGE, ".", SPECIAL_ENTER)
        )
    }


    fun getNumberKeys2(): List<List<String>> {
        return listOf(
            listOf("~", "`", "|", "•", "√", "π", "÷", "×", "§", "∆"),
            listOf("€", "¥", "$", "¢", "⁑", "°", "=", "{", "}", "\\"),
            listOf(SPECIAL_QNUMKEY, "%", "©", "®", "™", "✓", "[", "]", SPECIAL_BACK),
            listOf(SPECIAL_ABCKEY, SPECIAL_NUMKEY, "<", SPECIAL_LANGUAGE, ">", SPECIAL_ENTER)
        )
    }

    fun getSemiPureNumber(): List<List<String>> {
        return listOf(
            listOf("+","1", "2", "3", "%"),
            listOf("(","4", "5", "6", SPECIAL_DOUBLE_DASH),
            listOf(")","7", "8", "9", SPECIAL_BACK),
            listOf(SPECIAL_ABCKEY,SPECIAL_QONEHASH,",", SPECIAL_ZERO_PLUSH, ".","=", SPECIAL_ENTER)
        )
    }

    fun getPureNumber(): List<List<String>> {
        return listOf(
            listOf("1", "2", "3", SPECIAL_DASH),
            listOf("4", "5", "6", SPECIAL_DOUBLE_DASH),
            listOf("7", "8", "9", SPECIAL_BACK),
            listOf(SPECIAL_HASH_STAR, SPECIAL_ZERO_PLUSH, ".", SPECIAL_ARROW_RIGHT)
        )
    }


    fun getKeyboardKeys(
        isFirstCapsEnabled: Boolean,
        isCapsEnabled: Boolean,
        type: KeyboardType
    ): List<List<String>> {
        return when (type) {
            KeyboardType.LETTERS -> getAlphabetKeys(isFirstCapsEnabled, isCapsEnabled)
            KeyboardType.NUMBERS -> getNumberKeys()
            KeyboardType.NUMBERS2 -> getNumberKeys2()
            KeyboardType.EMOJI -> emptyList()
            KeyboardType.PURENUMBERS -> getPureNumber()
            KeyboardType.SEMIPURENUMBERS -> getSemiPureNumber()
        }
    }
}