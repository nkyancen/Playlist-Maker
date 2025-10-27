package ru.nkyancen.playlistmaker.presentation.main.model

sealed interface MainState {
    object TransitionToSearch: MainState

    object TransitionToMediaLibrary: MainState

    object TransitionToSettings: MainState
}