package ru.nkyancen.playlistmaker.utils

import android.util.TypedValue
import android.view.View

interface UnitsConverter {
    fun dpToPx(value: Float, targetView: View): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            targetView.resources.displayMetrics
        ).toInt()
    }
}