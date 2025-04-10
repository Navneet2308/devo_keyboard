package com.example.keyboard_app.android

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Handler
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat

import com.goodwy.lib.kotlin.tryOrNull
import java.lang.ref.WeakReference

private var FlorisApplicationReference = WeakReference<DevooApplication?>(null)

@Suppress("unused")
class DevooApplication : Application() {
    private val mainHandler by lazy { Handler(mainLooper) }

    override fun onCreate() {
        super.onCreate()
        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
    }


}

private tailrec fun Context.devooApplication(): DevooApplication {
    return when (this) {
        is DevooApplication -> this
        is ContextWrapper -> when {
            this.baseContext != null -> this.baseContext.devooApplication()
            else -> FlorisApplicationReference.get()!!
        }

        else -> tryOrNull { this.applicationContext as DevooApplication }
            ?: FlorisApplicationReference.get()!!
    }
}

fun Context.appContext() = lazyOf(this.devooApplication())



