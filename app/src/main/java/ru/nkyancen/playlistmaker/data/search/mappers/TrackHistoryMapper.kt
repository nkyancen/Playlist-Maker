package ru.nkyancen.playlistmaker.data.search.mappers

import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.data.search.dto.TrackHistory
import ru.nkyancen.playlistmaker.domain.search.models.Track

class TrackHistoryMapper: TrackMapper<TrackHistory> {
    override fun mapFromDomain(model: Track) =
        TrackHistory(
            id = model.id,
            trackName = model.trackName,
            artistName = model.artistName,
            trackTime = model.trackTime,
            albumPoster = model.albumPoster,
            albumName = model.albumName,
            releaseYear = model.releaseYear,
            genre = model.genre,
            country = model.country,
            preview = model.preview
        )

    override fun mapToDomain(dto: TrackHistory) = Track(
        id = dto.id,
        trackName = dto.trackName,
        artistName = dto.artistName,
        trackTime = dto.trackTime,
        albumPoster = dto.albumPoster,
        albumName = dto.albumName,
        releaseYear = dto.releaseYear,
        genre = dto.genre,
        country = dto.country,
        preview = dto.preview
    )

}