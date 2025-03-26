package com.goodwy.keyboard.lib.util
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.keyboard_app.android.BuildConfig
import com.goodwy.keyboard.lib.compose.observeAsState
import com.goodwy.keyboard.lib.devtools.flogDebug
import com.goodwy.lib.android.AndroidSettings
import com.goodwy.lib.android.AndroidVersion
import com.goodwy.lib.android.systemServiceOrNull
import kotlinx.coroutines.delay
private const val DELIMITER = ':'
private const val IME_SERVICE_CLASS_NAME = "KeyboardService"
private const val TIMED_QUERY_DELAY = 500L

object InputMethodUtils {
    fun isKeyboardEnabled(context: Context): Boolean {
        return if (AndroidVersion.ATLEAST_API34_U) {
            context.systemServiceOrNull(InputMethodManager::class)
                ?.enabledInputMethodList
                ?.any { it.packageName == BuildConfig.APPLICATION_ID } ?: false
        } else {
            val enabledImeList = AndroidSettings.Secure.getString(
                context, Settings.Secure.ENABLED_INPUT_METHODS
            )
            enabledImeList != null && parseIsKeyboardEnabled(context, enabledImeList)
        }
    }

    fun isKeyboardSelected(context: Context): Boolean {
        return if (AndroidVersion.ATLEAST_API34_U) {
            context.systemServiceOrNull(InputMethodManager::class)
                ?.currentInputMethodInfo
                ?.packageName == BuildConfig.APPLICATION_ID
        } else {
            val selectedIme = AndroidSettings.Secure.getString(
                context, Settings.Secure.DEFAULT_INPUT_METHOD
            )
            selectedIme != null && parseIsKeyboardSelected(context, selectedIme)
        }
    }

    @Composable
    fun observeIsKeyboardEnabled(
        context: Context = LocalContext.current.applicationContext,
        foregroundOnly: Boolean = false,
    ): State<Boolean> {
        return if (AndroidVersion.ATLEAST_API34_U) {
            timedObserveIsFlorisBoardEnabled()
        } else {
            AndroidSettings.Secure.observeAsState(
                key = Settings.Secure.ENABLED_INPUT_METHODS,
                foregroundOnly = foregroundOnly,
                transform = { parseIsKeyboardEnabled(context, it.toString()) },
            )
        }
    }

    @Composable
    fun observeIsKeyboardSelected(
        context: Context = LocalContext.current.applicationContext,
        foregroundOnly: Boolean = false,
    ): State<Boolean> {
        return if (AndroidVersion.ATLEAST_API34_U) {
            timedObserveIsFlorisBoardSelected()
        } else {
            AndroidSettings.Secure.observeAsState(
                key = Settings.Secure.DEFAULT_INPUT_METHOD,
                foregroundOnly = foregroundOnly,
                transform = { parseIsKeyboardSelected(context, it.toString()) },
            )
        }
    }

    fun parseIsKeyboardEnabled(context: Context, activeImeIds: String): Boolean {
        flogDebug { activeImeIds }
        return activeImeIds.split(DELIMITER).map { componentStr ->
            ComponentName.unflattenFromString(componentStr)
        }.any { it?.packageName == context.packageName && it?.className == IME_SERVICE_CLASS_NAME }
    }

    fun parseIsKeyboardSelected(context: Context, selectedImeId: String): Boolean {
        flogDebug { selectedImeId }
        val component = ComponentName.unflattenFromString(selectedImeId)
        return component?.packageName == context.packageName && component?.className == IME_SERVICE_CLASS_NAME
    }

    fun showImeEnablerActivity(context: Context) {
        val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
        }
        context.startActivity(intent)
    }

    fun showImePicker(context: Context): Boolean {
        val imm = context.systemServiceOrNull(InputMethodManager::class)
        return if (imm != null) {
            imm.showInputMethodPicker()
            true
        } else {
            false
        }
    }

    @RequiresApi(api = 34)
    @Composable
    internal fun timedObserveIsFlorisBoardEnabled(): State<Boolean> {
        val state = remember { mutableStateOf(false) }
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            while (true) {
                state.value = isKeyboardEnabled(context)
                delay(TIMED_QUERY_DELAY)
            }
        }
        return state
    }

    @RequiresApi(api = 34)
    @Composable
    private fun timedObserveIsFlorisBoardSelected(): State<Boolean> {
        val state = remember { mutableStateOf(false) }
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            while (true) {
                state.value = isKeyboardSelected(context)
                delay(TIMED_QUERY_DELAY)
            }
        }
        return state
    }
}
