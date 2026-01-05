package ru.nkyancen.playlistmaker.settings.domain.api

interface NightModeRepository {
    fun switchMode(isModeEnabled: Boolean)

    fun isNightModeFromSettings(): Boolean

    fun setNightMode()
}