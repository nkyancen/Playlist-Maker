package ru.nkyancen.playlistmaker.settings.domain.use_case

import ru.nkyancen.playlistmaker.settings.domain.api.NightModeInteractor
import ru.nkyancen.playlistmaker.settings.domain.api.NightModeRepository

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