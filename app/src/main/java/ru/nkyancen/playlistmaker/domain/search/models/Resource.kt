package ru.nkyancen.playlistmaker.domain.search.models

data class Resource<T>(
    val expression: String,
    val data: T
)