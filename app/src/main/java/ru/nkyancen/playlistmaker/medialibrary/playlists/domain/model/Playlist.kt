package ru.nkyancen.playlistmaker.medialibrary.playlists.domain.model

data class Playlist(
    val id: Long,
    val title: String,
    val description: String,
    val coverImage: String,
    val listOfTracks: String,
    val tracksAmount: Int
)
