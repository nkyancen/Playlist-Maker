package ru.nkyancen.playlistmaker.medialibrary.playlists.domain.impl

import android.net.Uri
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.ExternalStorageRepository
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistCoverInteractor

class PlaylistCoverInteractorImpl(
    private val externalStorageRepository: ExternalStorageRepository
): PlaylistCoverInteractor {
    override fun saveImageToStorage(imageUri: Uri, imageTitle: String) {
        externalStorageRepository.saveImageToStorage(imageUri, imageTitle)
    }

    override fun loadImageFromStorage(imageTitle: String): Uri =
        externalStorageRepository.loadImageFromStorage(imageTitle)

    override fun deleteImageFromStorage(imageTitle: String) {
        externalStorageRepository.deleteImageFromStorage(imageTitle)
    }
}