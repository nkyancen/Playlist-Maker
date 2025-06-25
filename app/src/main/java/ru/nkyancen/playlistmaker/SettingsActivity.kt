package ru.nkyancen.playlistmaker

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_setting)

        val buttonBack = findViewById<MaterialToolbar>(R.id.header_settings)

        buttonBack.setNavigationOnClickListener {
            finish()
        }

        val switcher = findViewById<SwitchCompat>(R.id.switch_dark_theme)

        switcher.isChecked = isNightThemeOn(this@SettingsActivity)

        switcher.setOnCheckedChangeListener { _, isChecked ->
            val currentNightMode = AppCompatDelegate.getDefaultNightMode()
            if (currentNightMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            } else if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun isNightThemeOn(context: Context): Boolean {
        val currentNightMode = AppCompatDelegate.getDefaultNightMode()

        return when (currentNightMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                true
            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
                false
            }

            else -> {
                val configuration = context.resources.configuration
                val uiMode = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

                uiMode == Configuration.UI_MODE_NIGHT_YES
            }
        }

    }
}