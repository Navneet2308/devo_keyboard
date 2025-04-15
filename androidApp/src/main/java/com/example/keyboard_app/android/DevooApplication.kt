package com.example.keyboard_app.android

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Handler
import com.example.keyboard_app.android.model.EmojiCategory

import com.goodwy.lib.kotlin.tryOrNull
import org.json.JSONObject
import java.io.InputStreamReader
import java.lang.ref.WeakReference

private var FlorisApplicationReference = WeakReference<DevooApplication?>(null)

@Suppress("unused")
class DevooApplication : Application() {
    private val mainHandler by lazy { Handler(mainLooper) }

    companion object {
        lateinit var emojiCategories: List<EmojiCategory>
            private set
    }

    override fun onCreate() {
        super.onCreate()

        loadEmojiCategories()

    }

    private fun loadEmojiCategories() {
        val inputStream = assets.open("emoji_categories.json")
        val jsonString = InputStreamReader(inputStream).use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        val categoriesArray = jsonObject.getJSONArray("categories")

        val categories = mutableListOf<EmojiCategory>()
        for (i in 0 until categoriesArray.length()) {
            val categoryObj = categoriesArray.getJSONObject(i)
            val icon = categoryObj.getString("icon")
            val emojisArray = categoryObj.getJSONArray("emojis")
            val emojis = mutableListOf<String>()
            for (j in 0 until emojisArray.length()) {
                emojis.add(emojisArray.getString(j))
            }
            categories.add(EmojiCategory(icon, emojis))
        }

        emojiCategories = categories
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



