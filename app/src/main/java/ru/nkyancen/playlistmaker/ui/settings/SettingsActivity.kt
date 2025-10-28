package ru.nkyancen.playlistmaker.ui.settings

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.databinding.ActivitySettingBinding
import ru.nkyancen.playlistmaker.presentation.settings.model.ExternalActionEventState
import ru.nkyancen.playlistmaker.presentation.settings.viewmodel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding


    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getFactory()
        ).get(SettingsViewModel::class.java)

        viewModel.observeNightMode().observe(this) {
            setSwitcherCheck(it.isNightMode)
        }

        binding.apply {
            settingsHeader.setNavigationOnClickListener {
                finish()
            }

            settingsNightModeSwitcher.setOnClickListener {
                viewModel.switchNightMode(
                    binding.settingsNightModeSwitcher.isChecked
                )
            }


            settingsShareButton.setOnClickListener {
                viewModel.executeExternalNavigation(
                    ExternalActionEventState.Share
                )
            }

            settingsSupportButton.setOnClickListener {
                viewModel.executeExternalNavigation(
                    ExternalActionEventState.Support
                )
            }


            settingsUserAgreementButton.setOnClickListener {
                viewModel.executeExternalNavigation(
                    ExternalActionEventState.Terms(
                        getString(R.string.offer_url)
                    )
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setSwitcherCheck(viewModel.getCurrentNightMode())
    }

    fun setSwitcherCheck(enabled: Boolean) {
        binding.settingsNightModeSwitcher.isChecked = enabled
    }

}