package com.example.keyboard_app.android
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.goodwy.lib.kotlin.CurlyArg
import com.goodwy.lib.kotlin.curlyFormat

private val LocalResourcesContext = staticCompositionLocalOf<Context> {
    error("resources context not initialized!!")
}

private val LocalAppNameString = staticCompositionLocalOf {
    "FlorisBoard"
}
@Composable
private fun formatString(
    string: String,
    args: Array<out CurlyArg>,
): String {
    return string.curlyFormat(
        "app_name" to LocalAppNameString.current,
        *args
    )
}


