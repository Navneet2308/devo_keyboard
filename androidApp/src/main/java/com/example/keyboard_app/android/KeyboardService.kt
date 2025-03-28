package com.example.keyboard_app.android

import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.keyboard_app.android.screens.ComposeKeyboardView

class KeyboardService : LifecycleInputMethodService(),
ViewModelStoreOwner,
SavedStateRegistryOwner {
    private var keyboardView: View? = null
    private val _isCapsEnabled = mutableStateOf(false)
    val isCapsEnabled: Boolean
        get() = _isCapsEnabled.value


    fun getKeys(): List<List<String>> =
        if (_isCapsEnabled.value == true) {
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

    fun toggleCaps() {
        _isCapsEnabled.value = !(_isCapsEnabled.value ?: false)
    }

    override fun onCreateInputView(): View {
        keyboardView = ComposeKeyboardView(this, ::getKeys)
        window?.window?.decorView?.let { decorView ->
            decorView.setViewTreeLifecycleOwner(this)
            decorView.setViewTreeViewModelStoreOwner(this)
            decorView.setViewTreeSavedStateRegistryOwner(this)
        }
        return keyboardView!!

    }

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
