package ru.nkyancen.playlistmaker.settings.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.nkyancen.playlistmaker.settings.domain.api.NightModeInteractor
import ru.nkyancen.playlistmaker.settings.domain.api.SharingInteractor
import ru.nkyancen.playlistmaker.settings.presentation.model.ExternalActionEventState
import ru.nkyancen.playlistmaker.settings.presentation.model.NightModeState

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val themeInteractor: NightModeInteractor
) : ViewModel() {
    private val nightModeLiveData = MutableLiveData<NightModeState>()
    fun observeNightMode(): LiveData<NightModeState> = nightModeLiveData

    init {
        loadSettings()
    }

    private fun loadSettings() {
        val isNightMode = getCurrentNightMode()
        nightModeLiveData.value = NightModeState(isNightMode = isNightMode)
    }

    fun switchNightMode(enabled: Boolean) {
        nightModeLiveData.value = NightModeState(isNightMode = enabled)
        themeInteractor.switchMode(enabled)
    }

    fun getCurrentNightMode(): Boolean = themeInteractor.isNightModeFromSettings()

    fun executeExternalNavigation(state: ExternalActionEventState) {
        when (state) {
            is ExternalActionEventState.Share -> sharingInteractor.shareApp()

            is ExternalActionEventState.Support -> sharingInteractor.openSupport()

            is ExternalActionEventState.Terms -> sharingInteractor.openTerms()
        }
    }
}