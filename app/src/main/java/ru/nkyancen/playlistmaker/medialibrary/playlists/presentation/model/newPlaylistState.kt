package ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model

import android.net.Uri

sealed interface NewPlaylistState {
    var isButtonEnable: Boolean
    var imageUri: Uri?

    data class Content(
        override var isButtonEnable: Boolean,
        override var imageUri: Uri?
    ) : NewPlaylistState

}