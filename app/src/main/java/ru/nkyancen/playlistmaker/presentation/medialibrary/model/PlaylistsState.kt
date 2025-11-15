package ru.nkyancen.playlistmaker.presentation.medialibrary.model

sealed interface PlaylistsState {
    object Empty: PlaylistsState
}