package ru.nkyancen.playlistmaker.generalViews

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
    private lateinit var backButton: MaterialToolbar

    private lateinit var themeSwitcher: SwitchMaterial

    private lateinit var shareButton: MaterialButton
    private lateinit var supportButton: MaterialButton
    private lateinit var userAgreementButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_setting)

        initializeViews()

        setClickListeners()
    }

    private fun initializeViews() {
        backButton = findViewById(R.id.settingsHeader)

        themeSwitcher = findViewById(R.id.darkThemeSwitch)

        shareButton = findViewById(R.id.shareButton)
        supportButton = findViewById(R.id.supportButton)
        userAgreementButton = findViewById(R.id.userAgreementButton)
    }

    private fun setClickListeners() {
        backButton.setNavigationOnClickListener {
            finish()
        }

        themeSwitcher.setOnClickListener {
            (applicationContext as App).switchDarkTheme(
                themeSwitcher.isChecked
            )
        }

        shareButton.setOnClickListener {
            createShareIntent()
        }


        supportButton.setOnClickListener {
            createSupportIntent()
        }


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