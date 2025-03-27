
package com.example.keyboard_app.android

import android.content.Context
import android.view.View
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import com.example.keyboard_app.android.theming.appName
import com.goodwy.lib.kotlin.CurlyArg
import com.goodwy.lib.kotlin.curlyFormat

private val LocalResourcesContext = staticCompositionLocalOf<Context> {
    error("resources context not initialized!!")
}

private val LocalAppNameString = staticCompositionLocalOf {
    "FlorisBoard"
}

@Composable
fun ProvideLocalizedResources(
    resourcesContext: Context,
    forceLayoutDirection: LayoutDirection? = null,
    content: @Composable () -> Unit,
) {
    val layoutDirection = forceLayoutDirection ?: when (resourcesContext.resources.configuration.layoutDirection) {
        View.LAYOUT_DIRECTION_LTR -> LayoutDirection.Ltr
        View.LAYOUT_DIRECTION_RTL -> LayoutDirection.Rtl
        else -> error("Given configuration specifies invalid layout direction!")
    }
    CompositionLocalProvider(
        LocalResourcesContext provides resourcesContext,
        LocalLayoutDirection provides layoutDirection,
        LocalAppNameString provides appName,
    ) {
        content()
    }
}

@Composable
fun stringRes(
    @StringRes id: Int,
    vararg args: CurlyArg,
): String {
    val string = LocalResourcesContext.current.resources
        .getString(id)
    return formatString(string, args)
}

@Composable
fun pluralsRes(
    @PluralsRes id: Int,
    quantity: Int,
    vararg args: CurlyArg,
): String {
    val string = LocalResourcesContext.current.resources
        .getQuantityString(id, quantity)
    return formatString(string, args)
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

//Goodwy
@Composable
fun stringResAddLockedLabelIfNeeded(
    @StringRes id: Int,
    needed: Boolean,
    vararg args: CurlyArg,
): String {
    val resources = LocalResourcesContext.current.resources
    val string = if (needed) "${resources.getString(id)} locked" else resources.getString(id)
    return formatString(string, args)
}
@Composable
fun stringAddLockedLabelIfNeeded(
    text: String,
    needed: Boolean,
    vararg args: CurlyArg,
): String {
    val resources = LocalResourcesContext.current.resources
    val string = if (needed) "$text locked" else text
    return formatString(string, args)
}
