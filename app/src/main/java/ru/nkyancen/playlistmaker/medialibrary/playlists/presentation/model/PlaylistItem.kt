package ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaylistItem(
    val id: Long,
    val title: String,
    val description: String,
    val coverImage: String,
    val listOfTracks: String = "",
    val tracksAmount: Int = 0
) : Parcelable
