package ru.nkyancen.playlistmaker.settings.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.databinding.FragmentSettingBinding
import ru.nkyancen.playlistmaker.settings.presentation.model.ExternalActionEventState
import ru.nkyancen.playlistmaker.settings.presentation.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.observeNightMode().observe(viewLifecycleOwner) {
            setSwitcherCheck(it.isNightMode)
        }

        binding.apply {
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