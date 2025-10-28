package ru.nkyancen.playlistmaker.presentation.settings.model

sealed interface ExternalActionEventState {
    object Share : ExternalActionEventState

    object Support : ExternalActionEventState

    data class Terms(val termsUrl: String) : ExternalActionEventState
}