package ru.nkyancen.playlistmaker.data.model

data class TrackSearchRequest(
    val expression: String
) : Request()