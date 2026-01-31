package ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model

import android.net.Uri

sealed interface NewPlaylistState {
    val isButtonEnable: Boolean
    val imageUri: Uri?

    data class Content(
        override val isButtonEnable: Boolean,
        override val imageUri: Uri?
    ) : NewPlaylistState

}