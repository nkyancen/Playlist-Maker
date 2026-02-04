package ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model

import android.net.Uri

sealed interface CreatePlaylistState {
    val isButtonEnable: Boolean
    val imageUri: Uri?

    data class Content(
        override val isButtonEnable: Boolean,
        override val imageUri: Uri?
    ) : CreatePlaylistState

    data class Init(
        val title: String,
        val description: String,
        override val isButtonEnable: Boolean = true,
        override val imageUri: Uri?
    ) : CreatePlaylistState

}