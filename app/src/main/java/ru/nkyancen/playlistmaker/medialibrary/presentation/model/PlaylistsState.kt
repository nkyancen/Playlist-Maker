package ru.nkyancen.playlistmaker.medialibrary.presentation.model

sealed interface PlaylistsState {
    object Empty: PlaylistsState
}