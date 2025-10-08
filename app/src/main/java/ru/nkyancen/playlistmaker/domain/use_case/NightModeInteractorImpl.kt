package ru.nkyancen.playlistmaker.domain.use_case

import ru.nkyancen.playlistmaker.domain.repository.night_mode.NightModeInteractor
import ru.nkyancen.playlistmaker.domain.repository.night_mode.NightModeRepository

class NightModeInteractorImpl(
    private val nightModeRepository: NightModeRepository
) : NightModeInteractor {
    override fun switchMode(isModeEnabled: Boolean) {
        nightModeRepository.switchMode(isModeEnabled)
    }

    override fun getSettingsValue() = nightModeRepository.getSettingsValue()

    override fun setNightMode() {
        nightModeRepository.setNightMode()
    }
}