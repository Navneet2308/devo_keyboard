package com.example.keyboard_app.android

import android.view.View
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.keyboard_app.android.screens.ComposeKeyboardView
import com.example.keyboard_app.android.utils.short_vibrate
import androidx.compose.runtime.State
class KeyboardService : LifecycleInputMethodService(),
    ViewModelStoreOwner,
    SavedStateRegistryOwner {


    private var keyboardView: View? = null
    private val _isCapsEnabled = mutableStateOf(false)
    val isCapsEnabled: State<Boolean> = _isCapsEnabled

    private val _isNumberKeyboard = mutableStateOf(false)
    val isNumberKeyboard: Boolean
        get() = _isNumberKeyboard.value

    private val _isEmojiKeyboard = mutableStateOf(false)
    val isEmojiKeyboard: Boolean
        get() = _isEmojiKeyboard.value


    private val _isNextLetterCaps = mutableStateOf(false)
    val isNextLetterCaps: State<Boolean> = _isNextLetterCaps

    private val _lastCapsTapTime = mutableStateOf(0L)
    val lastCapsTapTime: Long
        get() = _lastCapsTapTime.value


    fun resetCapsState() {
        _isCapsEnabled.value = false
        _isNextLetterCaps.value = false
        _lastCapsTapTime.value = 0L
    }

//    fun getKeys(): List<List<String>> =
//        when {
//            _isEmojiKeyboard.value -> {
//                emptyList()
//            }
//
//            _isNumberKeyboard.value -> {
//                listOf(
//                    listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
//                    listOf(" ", "@", "#", "$", "%", "&", "-", "+", "(", ")", " "),
//                    listOf("↑", "*", "'", ":", ";", "!", "?", "←"),
//                    listOf("?1#", ":)", ",", "language", ".", "⏎")
//                )
//            }
//
//            _isCapsEnabled.value == true ||   _isNextLetterCaps.value == true -> {
//                listOf(
//                    listOf("Q^1", "W^2", "E^3", "R^4", "T^5", "Y^6", "U^7", "I^8", "O^9", "P^0"),
//                    listOf(" ", "A^@", "S^#", "D^$", "F^%", "G^&", "H^-", "J^+", "K^(", "L^)", " "),
//                    listOf("↑", "Z^*", "X^", "C^'", "V^:", "B^;", "N^!", "M^?", "←"),
//                    listOf("?1#", ":)", ",", "language", ".", "⏎")
//                )
//            }
//
//            else -> {
//                listOf(
//                    listOf("q^1", "w^2", "e^3", "r^4", "t^5", "y^6", "u^7", "i^8", "o^9", "p^0"),
//                    listOf(" ", "a^@", "s^#", "d^$", "f^%", "g^&", "h^-", "j^+", "k^(", "l^)", " "),
//                    listOf("↑", "z^*", "x^", "c^'", "v^:", "b^;", "n^!", "m^?", "←"),
//                    listOf("?1#", ":)", ",", "language", ".", "⏎")
//                )
//            }
//        }

    fun changeNextLetterCaps() {
        println("changeNextLetterCaps" + " " + _isNextLetterCaps.value)
        println("changeLetterCaps" + " " + _isCapsEnabled.value)

        if (_isNextLetterCaps.value) {
            _isNextLetterCaps.value = _isNextLetterCaps.value.not()
        }
        println("changeNextLetterCaps" + " " + _isNextLetterCaps.value)
        println("changeLetterCaps" + " " + _isCapsEnabled.value)

    }

    fun toggleCaps() {
        val currentTime = System.currentTimeMillis()
        val timeDiff = currentTime - _lastCapsTapTime.value
        if (timeDiff < 300) {
            _isCapsEnabled.value = true
            _isNextLetterCaps.value = false
        } else {
            if (_isCapsEnabled.value) {
                _isCapsEnabled.value = false
            }
            else
            {
                _isNextLetterCaps.value = _isNextLetterCaps.value.not()
            }
        }
        println("changeNextLetterCaps" + " " + _isNextLetterCaps.value)
        println("changeLetterCaps" + " " + _isCapsEnabled.value)
        _lastCapsTapTime.value = currentTime
        short_vibrate(this)
    }



    fun switchToEmojiKeyboard() {
        _isEmojiKeyboard.value = !_isEmojiKeyboard.value
        _isNumberKeyboard.value = false
    }

    override fun onCreateInputView(): View {
        resetKeyboardState()
        keyboardView = ComposeKeyboardView(this)
        window?.window?.decorView?.let { decorView ->
            decorView.setViewTreeLifecycleOwner(this)
            decorView.setViewTreeViewModelStoreOwner(this)
            decorView.setViewTreeSavedStateRegistryOwner(this)
        }
        return keyboardView!!

    }

    private fun resetKeyboardState() {
        _isCapsEnabled.value = false
        _isNumberKeyboard.value = false
        _isEmojiKeyboard.value = false
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)

    }

    override val viewModelStore: ViewModelStore
        get() = store
    override val lifecycle: Lifecycle
        get() = dispatcher.lifecycle


    private val store = ViewModelStore()


    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry
}
