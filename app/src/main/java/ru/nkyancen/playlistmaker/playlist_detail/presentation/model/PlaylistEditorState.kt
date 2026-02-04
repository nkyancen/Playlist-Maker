package ru.nkyancen.playlistmaker.playlist_detail.presentation.model

import android.net.Uri

sealed interface PlaylistEditorState {
    val isButtonEnable: Boolean
    val uri: Uri?

    data class Content(
        override val isButtonEnable: Boolean = true,
        override val uri: Uri?
    ) :
        PlaylistEditorState

}