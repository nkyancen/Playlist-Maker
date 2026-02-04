package ru.nkyancen.playlistmaker.player.presentation.model

import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem

sealed interface PlayerBottomSheetState {
    data class Show(val playlists: List<PlaylistItem>): PlayerBottomSheetState

    object Hide: PlayerBottomSheetState
}