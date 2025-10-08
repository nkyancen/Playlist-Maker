package ru.nkyancen.playlistmaker.domain.repository.night_mode

interface NightModeRepository {
    fun switchMode(isModeEnabled: Boolean)

    fun getSettingsValue(): Boolean

    fun setNightMode()
}