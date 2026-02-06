package ru.nkyancen.playlistmaker.playlist_detail.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_table")
data class TrackEntity(
    @PrimaryKey
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
)