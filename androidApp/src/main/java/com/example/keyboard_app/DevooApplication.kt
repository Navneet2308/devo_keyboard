
package com.example.keyboard_app
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import com.goodwy.lib.kotlin.tryOrNull
import java.lang.ref.WeakReference
private var FlorisApplicationReference = WeakReference<DevooApplication?>(null)

@Suppress("unused")
class DevooApplication : Application() {
}
    private tailrec fun Context.florisApplication(): DevooApplication {
        return when (this) {
            is DevooApplication -> this
            is ContextWrapper -> when {
                this.baseContext != null -> this.baseContext.florisApplication()
                else -> FlorisApplicationReference.get()!!
            }

            else -> tryOrNull { this.applicationContext as DevooApplication }
                ?: FlorisApplicationReference.get()!!
        }
    }

fun Context.appContext() = lazyOf(this.florisApplication())




