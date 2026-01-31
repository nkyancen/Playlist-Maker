package ru.nkyancen.playlistmaker.medialibrary.playlists.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.ExternalStorageRepository
import java.io.File
import java.io.FileOutputStream


class ExternalStorageRepositoryImpl(
    private val appContext: Context
): ExternalStorageRepository {
    override fun loadImageFromStorage(imageTitle: String): Uri {
        val filePath = File(
            appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PLAYLIST_COVERS_FOLDER
        )
        val file = File(filePath, imageTitle)

        return file.toUri()
    }

    override fun saveImageToStorage(imageUri: Uri, imageTitle: String) {
        val filePath = File(
            appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PLAYLIST_COVERS_FOLDER
        )

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, imageTitle)

        val inputStream = appContext.contentResolver.openInputStream(imageUri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    companion object{
        const val PLAYLIST_COVERS_FOLDER = "playlist_covers"
    }
}