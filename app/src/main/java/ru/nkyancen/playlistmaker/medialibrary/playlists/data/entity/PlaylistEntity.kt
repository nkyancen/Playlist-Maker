package ru.nkyancen.playlistmaker.medialibrary.playlists.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val description: String,
    val coverImage: String,
    val listOfTracksId: String = "",
    val tracksAmount: Int = 0
)
