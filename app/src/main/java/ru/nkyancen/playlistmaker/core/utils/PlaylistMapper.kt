package ru.nkyancen.playlistmaker.core.utils

import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.model.Playlist

interface PlaylistMapper<T> {
    fun mapFromDomain(model: Playlist): T

    fun mapListFromDomain(models: List<Playlist>): List<T> = models.map {
        mapFromDomain(it)
    }

    fun mapToDomain(dto: T): Playlist

    fun mapListToDomain(dtoList: List<T>) : List<Playlist> = dtoList.map {
        mapToDomain(it)
    }
}