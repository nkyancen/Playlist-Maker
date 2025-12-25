package ru.nkyancen.playlistmaker.settings.presentation.model

sealed interface ExternalActionEventState {
    object Share : ExternalActionEventState

    object Support : ExternalActionEventState

    data class Terms(val termsUrl: String) : ExternalActionEventState
}