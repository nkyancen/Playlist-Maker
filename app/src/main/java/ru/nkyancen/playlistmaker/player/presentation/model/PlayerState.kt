package ru.nkyancen.playlistmaker.player.presentation.model

sealed interface PlayerState {
    val progressTime: String
    val isFavorites: Boolean

    data class Play(override val progressTime: String, override val isFavorites: Boolean) : PlayerState

    data class Pause(override val progressTime: String, override val isFavorites: Boolean) : PlayerState

    data class Prepared(override val progressTime: String, override val isFavorites: Boolean) : PlayerState
}