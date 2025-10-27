package ru.nkyancen.playlistmaker.presentation.search.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackItem(
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: Long,
    val albumPoster: String,
    val albumName: String,
    val releaseYear: String,
    val genre: String,
    val country: String,
    val preview: String
) : Parcelable {
    fun getPlayerAlbumPoster() = albumPoster.replaceAfterLast('/', "512x512bb.jpg")
}