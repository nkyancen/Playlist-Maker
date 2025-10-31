package ru.nkyancen.playlistmaker.data.search.mappers

import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.data.search.dto.TrackData
import ru.nkyancen.playlistmaker.domain.search.models.Track

class TrackDataMapper : TrackMapper<TrackData> {
    override fun mapFromDomain(model: Track) =
        TrackData(
            id = model.id,
            trackName = model.trackName,
            artistName = model.artistName,
            trackTime = model.trackTime,
            albumPosterUrl = model.albumPoster,
            albumName = model.albumName,
            releaseDate = model.releaseYear,
            genre = model.genre,
            country = model.country,
            previewUrl = model.preview
        )

    override fun mapToDomain(dto: TrackData) = Track(
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
}