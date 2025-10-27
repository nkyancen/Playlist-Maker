package ru.nkyancen.playlistmaker.data.search.dto

data class TrackSearchRequest(
    val expression: String
) : Request()