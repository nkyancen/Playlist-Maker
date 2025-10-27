package ru.nkyancen.playlistmaker.domain.settings.api

interface NightModeInteractor {
    fun switchMode(isModeEnabled: Boolean)

    fun isNightModeFromSettings(): Boolean

    fun setNightMode()
}