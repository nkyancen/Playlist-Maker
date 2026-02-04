package ru.nkyancen.playlistmaker.playlist_detail.presentation.model

sealed interface PlaylistDetailsMenuBottomSheetState {
    object Show : PlaylistDetailsMenuBottomSheetState

    object Hide : PlaylistDetailsMenuBottomSheetState
}