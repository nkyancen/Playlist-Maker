package ru.nkyancen.playlistmaker.medialibrary.favorites.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_table")
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
    val preview: String,
    val createdTime: Long = System.currentTimeMillis()
)
