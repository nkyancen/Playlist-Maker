package ru.nkyancen.playlistmaker.core.utils

import ru.nkyancen.playlistmaker.search.domain.models.Track

interface TrackMapper<T> {
    fun mapFromDomain(model: Track): T

    fun mapListFromDomain(models: List<Track>): List<T> = models.map {
        mapFromDomain(it)
    }

    fun mapToDomain(dto: T): Track

    fun mapListToDomain(dtoList: List<T>) : List<Track> = dtoList.map {
        mapToDomain(it)
    }
}