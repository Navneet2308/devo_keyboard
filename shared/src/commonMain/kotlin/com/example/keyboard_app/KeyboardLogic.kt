package com.example.keyboard_app

class KeyboardLogic {
    private var isUppercase: Boolean = false
    fun toggleCase(): Boolean {
        isUppercase = !isUppercase
        return isUppercase
    }

    fun processKey(key: String): String {
        return if (isUppercase) key.uppercase() else key.lowercase()
    }
    fun insertSpace(): String {
        return " "
    }
    fun deleteCharacter(): Pair<Int, Int> {
        return Pair(1, 0)
    }
}