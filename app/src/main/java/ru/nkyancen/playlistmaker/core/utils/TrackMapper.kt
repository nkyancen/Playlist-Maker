package ru.nkyancen.playlistmaker.core.utils

import ru.nkyancen.playlistmaker.domain.search.models.Track

interface TrackMapper<T> {
    fun mapFromDomain(model: Track): T

    fun mapListFromDomain(models: List<Track>): List<T> = models.map {
        mapFromDomain(it)
    }

    fun mapToDomain(dto: T): Track

    fun mapListToDomain(dtos: List<T>) : List<Track> = dtos.map {
        mapToDomain(it)
    }
}