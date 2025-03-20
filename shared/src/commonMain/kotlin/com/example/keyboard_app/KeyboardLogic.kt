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
        return " "   // Shared logic to return a space character
    }
    fun deleteCharacter(): Pair<Int, Int> {
        // Return the number of characters to delete before and after the cursor
        return Pair(1, 0)  // 1 character before, 0 after
    }
}