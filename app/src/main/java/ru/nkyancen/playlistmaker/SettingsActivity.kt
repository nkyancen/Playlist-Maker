package ru.nkyancen.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.net.toUri
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_setting)

        val buttonBack = findViewById<MaterialToolbar>(R.id.header_settings)

        buttonBack.setNavigationOnClickListener {
            finish()
        }

        val switcher = findViewById<SwitchCompat>(R.id.switch_dark_theme)

        switcher.isChecked = isDarkMode(this@SettingsActivity)

        switcher.setOnClickListener {
            when (switcher.isChecked) {
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val buttonShare = findViewById<MaterialButton>(R.id.button_share)

        buttonShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("plain/text")
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.share_text)
            )
            startActivity(Intent.createChooser(shareIntent, null))
        }

        val buttonSupport = findViewById<MaterialButton>(R.id.button_support)

        buttonSupport.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = "mailto:".toUri()
            supportIntent.putExtra(
                Intent.EXTRA_EMAIL,
                resources.getStringArray(R.array.email)
            )
            supportIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.mail_title)
            )
            supportIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.mail_text)
            )
            startActivity(supportIntent)
        }

        val buttonUserAgreement = findViewById<MaterialButton>(R.id.button_user_agreement)

        buttonUserAgreement.setOnClickListener {
            val userAgreementIntent = Intent(Intent.ACTION_VIEW)
            userAgreementIntent.data = "https://yandex.ru/legal/practicum_offer/".toUri()
            startActivity(userAgreementIntent)
        }
    }

    private fun isDarkMode(context: Context): Boolean {
        val darkModeFlag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }
}