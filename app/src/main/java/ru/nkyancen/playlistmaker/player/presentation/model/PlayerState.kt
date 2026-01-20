package ru.nkyancen.playlistmaker.player.presentation.model

sealed interface PlayerState {
    val progressTime: String
    var isFavorites: Boolean

    data class Play(override val progressTime: String, override var isFavorites: Boolean) : PlayerState

    data class Pause(override val progressTime: String, override var isFavorites: Boolean) : PlayerState

    data class Prepared(override val progressTime: String, override var isFavorites: Boolean) : PlayerState
}