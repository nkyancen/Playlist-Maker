package ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model

sealed interface PlaylistsState {
    object Empty: PlaylistsState

    object Loading: PlaylistsState

    data class Content(
        val playlists: List<PlaylistItem>
    ) : PlaylistsState
}