package ru.nkyancen.playlistmaker.playlist_detail.presentation.model

import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem

sealed interface PlaylistDetailsBottomSheetState {

    object Loading : PlaylistDetailsBottomSheetState

    data class Content(val tracks: List<TrackItem>) : PlaylistDetailsBottomSheetState

    object Empty : PlaylistDetailsBottomSheetState

}