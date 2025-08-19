package ru.nkyancen.playlistmaker.utils

import android.util.TypedValue
import android.view.View
import java.text.SimpleDateFormat
import java.util.Locale

interface Converter {
    fun dpToPx(value: Float, targetView: View): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            targetView.resources.displayMetrics
        ).toInt()
    }

    fun formatTime(value: Long?): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(value ?: 0L)
}