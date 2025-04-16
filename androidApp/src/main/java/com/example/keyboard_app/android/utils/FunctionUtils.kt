package com.example.keyboard_app.android.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

fun short_vibrate(context: Context?) {
    context?.let {
        val vibrator = it.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.let { vib ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vib.vibrate(40)
            }
        }
    }
}

