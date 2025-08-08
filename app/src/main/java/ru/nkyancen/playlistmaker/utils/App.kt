package ru.nkyancen.playlistmaker.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

const val IS_DARK_THEME_TAG = "Is Dark Theme On"

class App : Application() {
    var isDarkTheme = false

    lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(IS_DARK_THEME_TAG, MODE_PRIVATE)

        applyDarkTheme()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        applyDarkTheme()
    }

    fun applyDarkTheme() {
        isDarkTheme = sharedPrefs.getBoolean(
            IS_DARK_THEME_TAG,
            isDarkMode(applicationContext as App)
        )

        setTheme()
    }

    fun isDarkMode(context: Context): Boolean {
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

    fun setTheme() {
        AppCompatDelegate.setDefaultNightMode(
            when (isDarkTheme) {
                true -> AppCompatDelegate.MODE_NIGHT_YES
                false -> AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}