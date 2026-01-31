package ru.nkyancen.playlistmaker.medialibrary.playlists.domain.impl

import android.net.Uri
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.ExternalStorageInteractor
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.ExternalStorageRepository

class ExternalStorageInteractorImpl(
    private val externalStorageRepository: ExternalStorageRepository
): ExternalStorageInteractor {
    override fun saveImageToStorage(imageUri: Uri, imageTitle: String) {
        externalStorageRepository.saveImageToStorage(imageUri, imageTitle)
    }

    override fun loadImageFromStorage(imageTitle: String): Uri =
        externalStorageRepository.loadImageFromStorage(imageTitle)

}