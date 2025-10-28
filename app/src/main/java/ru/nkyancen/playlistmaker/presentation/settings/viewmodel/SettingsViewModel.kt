package ru.nkyancen.playlistmaker.presentation.settings.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.nkyancen.playlistmaker.core.creator.Creator
import ru.nkyancen.playlistmaker.domain.settings.api.NightModeInteractor
import ru.nkyancen.playlistmaker.domain.sharing.api.SharingInteractor
import ru.nkyancen.playlistmaker.presentation.settings.model.ExternalActionEventState
import ru.nkyancen.playlistmaker.presentation.settings.model.NightModeState

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

    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val sharingInteractor = Creator.provideSharingInteractor()
                val themeInteractor = Creator.provideNightModeInteractor()
                SettingsViewModel(sharingInteractor, themeInteractor)
            }
        }
    }
}