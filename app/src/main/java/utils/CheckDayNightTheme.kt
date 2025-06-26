package utils

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

interface CheckDayNightTheme {

    fun applyNightMode(context: Context) {
        val sharedPreferences = context.getSharedPreferences(
            SHARED_SETTINGS,
            Context.MODE_PRIVATE
        )

        sharedPreferences.edit {

            val configuration = context.resources.configuration
            val uiMode = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

            val isSystemDarkModeOn = uiMode == Configuration.UI_MODE_NIGHT_YES

            putBoolean(IS_SYSTEM_MODE, isSystemDarkModeOn)
        }

        val isDarkModeOn = sharedPreferences.getBoolean(IS_DARK_MODE, false)

        val isSwitcherUse = sharedPreferences.getBoolean(IS_SWITCH_USE, false)

        if (!isSwitcherUse) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        } else if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    companion object {
        const val SHARED_SETTINGS = "my_settings"
        const val IS_DARK_MODE = "isDarkModeOn"
        const val IS_SYSTEM_MODE = "isSystemDarkModeOn"
        const val IS_SWITCH_USE = "isSwitchUse"
    }
}
