package ru.nkyancen.playlistmaker.generalView

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.utils.App

class SettingsActivity : AppCompatActivity() {
    private lateinit var themeSwitcher: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_setting)

        val backButton = findViewById<MaterialToolbar>(R.id.settingsHeader)

        backButton.setNavigationOnClickListener {
            finish()
        }

        themeSwitcher = findViewById(R.id.darkThemeSwitch)

        themeSwitcher.setOnClickListener {
            (applicationContext as App).switchDarkTheme(
                themeSwitcher.isChecked
            )
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
        shareIntent.type = "plain/text"
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
        userAgreementIntent.data = getString(R.string.oferta_url).toUri()
        startActivity(userAgreementIntent)
    }

    override fun onResume() {
        super.onResume()
        themeSwitcher.isChecked = (applicationContext as App).isDarkTheme(this)
    }
}