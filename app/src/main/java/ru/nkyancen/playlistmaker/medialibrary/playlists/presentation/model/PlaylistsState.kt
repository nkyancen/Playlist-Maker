package ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model

sealed interface PlaylistsState {
    object Empty: PlaylistsState
}