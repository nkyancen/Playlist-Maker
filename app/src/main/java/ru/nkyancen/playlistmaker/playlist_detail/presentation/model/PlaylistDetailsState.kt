package ru.nkyancen.playlistmaker.playlist_detail.presentation.model

import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem

sealed interface PlaylistDetailsState {
    object Loading : PlaylistDetailsState

    data class Content(
        val playlist: PlaylistItem,
        val totalDuration: Int
    ) : PlaylistDetailsState

}