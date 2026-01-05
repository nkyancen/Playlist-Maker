package ru.nkyancen.playlistmaker.player.presentation.model

sealed interface PlayerState {
    val progressTime: String

    data class Play(override val progressTime: String) : PlayerState

    data class Pause(override val progressTime: String) : PlayerState

    data class Prepared(override val progressTime: String) : PlayerState
}