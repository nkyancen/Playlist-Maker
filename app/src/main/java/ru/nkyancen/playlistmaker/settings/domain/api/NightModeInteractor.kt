package ru.nkyancen.playlistmaker.settings.domain.api

interface NightModeInteractor {
    fun switchMode(isModeEnabled: Boolean)

    fun isNightModeFromSettings(): Boolean

    fun setNightMode()
}