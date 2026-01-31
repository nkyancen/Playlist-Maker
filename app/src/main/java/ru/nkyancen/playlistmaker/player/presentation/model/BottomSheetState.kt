package ru.nkyancen.playlistmaker.player.presentation.model

import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem

sealed interface BottomSheetState {
    data class Show(val playlists: List<PlaylistItem>): BottomSheetState

    object Hide: BottomSheetState
}