package ru.nkyancen.playlistmaker.data.search.dto

data class TrackHistory(
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