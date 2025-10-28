package ru.nkyancen.playlistmaker.presentation.player.model

sealed interface PlayerState {
    object Play : PlayerState

    object Pause : PlayerState

    object Prepared : PlayerState
}