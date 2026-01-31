package ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api

import android.net.Uri

interface PlaylistCoverInteractor {

    fun saveImageToStorage(imageUri: Uri, imageTitle: String)

    fun loadImageFromStorage(imageTitle: String): Uri
}