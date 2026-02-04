package ru.nkyancen.playlistmaker.settings.presentation.model

sealed interface NightModeState {
    object Day: NightModeState
    object Night: NightModeState
}
