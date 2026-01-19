package ru.nkyancen.playlistmaker.search.domain.models

data class Track(
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
    var isFavorite: Boolean = false
)