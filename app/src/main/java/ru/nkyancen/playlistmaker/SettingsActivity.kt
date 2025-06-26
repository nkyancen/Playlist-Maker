package ru.nkyancen.playlistmaker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.edit
import com.google.android.material.appbar.MaterialToolbar
import utils.CheckDayNightTheme

class SettingsActivity : AppCompatActivity(), CheckDayNightTheme {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_setting)

        val buttonBack = findViewById<MaterialToolbar>(R.id.header_settings)

        buttonBack.setNavigationOnClickListener {
            finish()
        }

        applyNightMode(this@SettingsActivity)

        val switcher = findViewById<SwitchCompat>(R.id.switch_dark_theme)

        val sharedPreferences =
            getSharedPreferences(
                CheckDayNightTheme.SHARED_SETTINGS,
                MODE_PRIVATE
            )

        setSwitcherCheck(switcher)

        switcher.setOnClickListener {
            sharedPreferences.edit {
                putBoolean(
                    CheckDayNightTheme.IS_SWITCH_USE,
                    true
                )
                if (switcher.isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    putBoolean(
                        CheckDayNightTheme.IS_DARK_MODE,
                        true
                    )
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    putBoolean(
                        CheckDayNightTheme.IS_DARK_MODE,
                        false
                    )
                }
            }
        }
    }

    private fun setSwitcherCheck(switcher: SwitchCompat) {
        val sharedPreferences =
            getSharedPreferences(
                CheckDayNightTheme.SHARED_SETTINGS,
                MODE_PRIVATE
            )

        val isSystemDarkModeOn = sharedPreferences.getBoolean(
            CheckDayNightTheme.IS_SYSTEM_MODE,
            false
        )

        val isDarkModeOn = sharedPreferences.getBoolean(
            CheckDayNightTheme.IS_DARK_MODE,
            false
        )

        val isSwitchUse = sharedPreferences.getBoolean(
            CheckDayNightTheme.IS_SWITCH_USE,
            false
        )

        switcher.isChecked = (isSystemDarkModeOn and !isSwitchUse) or (isDarkModeOn and isSwitchUse)
    }
}