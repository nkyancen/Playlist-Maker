package ru.nkyancen.playlistmaker.domain.repository.night_mode

interface NightModeInteractor {
    fun switchMode(isModeEnabled: Boolean)

    fun getSettingsValue(): Boolean

    fun setNightMode()
}