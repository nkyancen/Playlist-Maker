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
    private var switcher: SwitchCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_setting)

        val backButton = findViewById<MaterialToolbar>(R.id.settingsHeader)

        backButton.setNavigationOnClickListener {
            finish()
        }

        switcher = findViewById(R.id.darkThemeSwitch)

        switcher?.setOnClickListener {
            when (switcher?.isChecked) {
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                else -> {}
            }
        }

        val shareButton = findViewById<MaterialButton>(R.id.shareButton)

        shareButton.setOnClickListener {
            createShareIntent()
        }

        val supportButton = findViewById<MaterialButton>(R.id.supportButton)

        supportButton.setOnClickListener {
            createSupportIntent()
        }

        val userAgreementButton = findViewById<MaterialButton>(R.id.userAgreementButton)

        userAgreementButton.setOnClickListener {
            createAgreementIntent()
        }
    }

    private fun createShareIntent() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("plain/text")
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.share_text)
        )
        startActivity(Intent.createChooser(shareIntent, null))
    }

    private fun createSupportIntent() {
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

    private fun createAgreementIntent() {
        val userAgreementIntent = Intent(Intent.ACTION_VIEW)
        userAgreementIntent.data = "https://yandex.ru/legal/practicum_offer/".toUri()
        startActivity(userAgreementIntent)
    }

    override fun onResume() {
        super.onResume()
        switcher?.isChecked = isDarkMode(this@SettingsActivity)
    }

    private fun isDarkMode(context: Context): Boolean {
        val darkModeFlag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }
}