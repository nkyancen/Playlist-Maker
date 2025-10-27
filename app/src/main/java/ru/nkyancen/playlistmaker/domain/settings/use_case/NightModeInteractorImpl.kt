package ru.nkyancen.playlistmaker.domain.settings.use_case

import ru.nkyancen.playlistmaker.domain.settings.api.NightModeInteractor
import ru.nkyancen.playlistmaker.domain.settings.api.NightModeRepository

class NightModeInteractorImpl(
    private val nightModeRepository: NightModeRepository
) : NightModeInteractor {
    override fun switchMode(isModeEnabled: Boolean) {
        nightModeRepository.switchMode(isModeEnabled)
    }

    override fun isNightModeFromSettings() = nightModeRepository.isNightModeFromSettings()

    override fun setNightMode() {
        nightModeRepository.setNightMode()
    }
}