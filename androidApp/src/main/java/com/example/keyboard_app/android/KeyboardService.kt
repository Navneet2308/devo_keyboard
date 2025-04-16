package com.example.keyboard_app.android

import android.text.InputType
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.keyboard_app.android.screens.ComposeKeyboardView
import com.example.keyboard_app.android.utils.short_vibrate
import androidx.compose.runtime.State
import com.example.keyboard_app.android.ime.KeyboardType

class KeyboardService : LifecycleInputMethodService(),
    ViewModelStoreOwner,
    SavedStateRegistryOwner {
    private var keyboardView: View? = null
    private val _isCapsEnabled = mutableStateOf(false)
    val isCapsEnabled: State<Boolean> = _isCapsEnabled


    private val _keyboardType = mutableStateOf(KeyboardType.LETTERS)
    val keyboardType: State<KeyboardType> = _keyboardType


    private val _isNumberKeyboard = mutableStateOf(false)
    val isNumberKeyboard: Boolean
        get() = _isNumberKeyboard.value

    private val _isEmojiKeyboard = mutableStateOf(false)
    val isEmojiKeyboard: Boolean
        get() = _isEmojiKeyboard.value


    private val _isNextLetterCaps = mutableStateOf(false)
    val isNextLetterCaps: State<Boolean> = _isNextLetterCaps

    private val _lastCapsTapTime = mutableStateOf(0L)

    fun changeNextLetterCaps() {
        println("changeNextLetterCaps" + " " + _isNextLetterCaps.value)
        println("changeLetterCaps" + " " + _isCapsEnabled.value)

        if (_isNextLetterCaps.value) {
            _isNextLetterCaps.value = _isNextLetterCaps.value.not()
        }
        println("changeNextLetterCaps" + " " + _isNextLetterCaps.value)
        println("changeLetterCaps" + " " + _isCapsEnabled.value)

    }

    fun toggle()
    {
        _isCapsEnabled.value = true
        _isNextLetterCaps.value = false
    }

    fun toggleCaps() {
        val currentTime = System.currentTimeMillis()
        val timeDiff = currentTime - _lastCapsTapTime.value
        println("timeDifftimeDiff"+timeDiff)
        if (timeDiff < 300) {
            _isCapsEnabled.value = true
            _isNextLetterCaps.value = false
        } else {
            if (_isCapsEnabled.value) {
                _isCapsEnabled.value = false
            } else {
                _isNextLetterCaps.value = _isNextLetterCaps.value.not()
            }
        }
        _lastCapsTapTime.value = currentTime
        println("_lastCapsTapTime"+_lastCapsTapTime.value.toString())
        short_vibrate(this)
    }


    override fun onCreateInputView(): View {
        keyboardView = ComposeKeyboardView(this)
        window?.window?.decorView?.let { decorView ->
            decorView.setViewTreeLifecycleOwner(this)
            decorView.setViewTreeViewModelStoreOwner(this)
            decorView.setViewTreeSavedStateRegistryOwner(this)
        }
        return keyboardView!!

    }


    // In KeyboardService.kt
    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        resetKeyboardState()
        val inputType = info?.inputType ?: 0
        when (inputType and InputType.TYPE_MASK_CLASS) {
            InputType.TYPE_CLASS_NUMBER -> {
                _keyboardType.value = KeyboardType.PURENUMBERS
                Log.d("KeyboardTyp", "Number input detected")
            }

            InputType.TYPE_CLASS_TEXT -> {
                _keyboardType.value = KeyboardType.LETTERS

                Log.d("KeyboardTyp", "Text input detected")
                when (inputType and InputType.TYPE_MASK_VARIATION) {
                    InputType.TYPE_TEXT_VARIATION_PASSWORD -> {
                        Log.d("KeyboardTyp", "Password input")
                    }

                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS -> {
                        Log.d("KeyboardTyp", "Email input")
                    }
                }
            }

            InputType.TYPE_CLASS_PHONE -> {
                _keyboardType.value = KeyboardType.PURENUMBERS
                Log.d("KeyboardTyp", "Phone input detected")
            }
        }

        (keyboardView as? ComposeKeyboardView)?.apply {
            disposeComposition()
            createComposition()
        }
    }

    fun resetKeyboardState() {
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
