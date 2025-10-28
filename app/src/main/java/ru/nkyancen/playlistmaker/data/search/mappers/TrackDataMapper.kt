package ru.nkyancen.playlistmaker.data.search.mappers

import ru.nkyancen.playlistmaker.data.search.dto.TrackData
import ru.nkyancen.playlistmaker.domain.search.models.Track

class TrackDataMapper  {
    fun mapToDomain(dto: TrackData) = Track(
        id = dto.id,
        trackName = dto.trackName ?: "",
        artistName = dto.artistName ?: "",
        trackTime = dto.trackTime ?: 0L,
        albumPoster = dto.albumPosterUrl ?: "",
        albumName = dto.albumName ?: "",
        releaseYear = dto.getTrackYear(),
        genre = dto.genre ?: "",
        country = dto.country ?: "",
        preview = dto.previewUrl ?: ""
    )

    fun mapListToDomain(dtoList: List<TrackData>) = dtoList.map {
        mapToDomain(it)
    }

}