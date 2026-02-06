package ru.nkyancen.playlistmaker.settings.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.nkyancen.playlistmaker.settings.domain.api.NightModeInteractor
import ru.nkyancen.playlistmaker.settings.domain.api.SharingInteractor
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
        if (getCurrentNightMode()) {
            renderNightState(
                NightModeState.Night
            )
        } else {
            renderNightState(
                NightModeState.Day
            )
        }
    }

    fun switchNightMode(enabled: Boolean) {
        if (enabled) {
            renderNightState(
                NightModeState.Night
            )
        } else {
            renderNightState(
                NightModeState.Day
            )
        }

        themeInteractor.switchMode(enabled)
    }

    fun renderNightState(state: NightModeState) {
        nightModeLiveData.postValue(state)
    }

    fun getCurrentNightMode(): Boolean = themeInteractor.isNightModeFromSettings()

    fun shareApp(sharedUrl: String) {
        sharingInteractor.shareApp(sharedUrl)
    }

    fun sendMailToSupport() {
        sharingInteractor.openSupport()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

}