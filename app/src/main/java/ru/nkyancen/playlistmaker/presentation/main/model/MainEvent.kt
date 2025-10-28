package ru.nkyancen.playlistmaker.presentation.main.model

sealed interface MainEvent {
    object TransitionToSearch: MainEvent

    object TransitionToMediaLibrary: MainEvent

    object TransitionToSettings: MainEvent
}