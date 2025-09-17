package ru.nkyancen.playlistmaker.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

const val IS_DARK_THEME_TAG = "Is Dark Theme On"

class App : Application() {
    private var isDarkTheme = false

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(IS_DARK_THEME_TAG, MODE_PRIVATE)

        applyTheme()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        applyTheme()
    }

    fun isDarkTheme(context: Context): Boolean {
        val darkModeFlag =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }

    fun switchDarkTheme(isDarkThemeEnabled: Boolean) {
        isDarkTheme = isDarkThemeEnabled

        sharedPrefs.edit {
            putBoolean(
                IS_DARK_THEME_TAG,
                isDarkTheme
            )
        }

        setTheme()
    }

    private fun applyTheme() {
        isDarkTheme = sharedPrefs.getBoolean(
            IS_DARK_THEME_TAG,
            isDarkTheme(applicationContext as App)
        )

        setTheme()
    }

    private fun setTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}