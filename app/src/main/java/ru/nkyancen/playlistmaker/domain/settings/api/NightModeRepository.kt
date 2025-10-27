package ru.nkyancen.playlistmaker.domain.settings.api

interface NightModeRepository {
    fun switchMode(isModeEnabled: Boolean)

    fun isNightModeFromSettings(): Boolean

    fun setNightMode()
}