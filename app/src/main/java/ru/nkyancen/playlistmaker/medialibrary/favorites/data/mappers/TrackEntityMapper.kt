package ru.nkyancen.playlistmaker.medialibrary.favorites.data.mappers

import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.entity.TrackEntity
import ru.nkyancen.playlistmaker.search.domain.models.Track

class TrackEntityMapper: TrackMapper<TrackEntity> {
    override fun mapFromDomain(model: Track) =
        TrackEntity(
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

    override fun mapToDomain(dto: TrackEntity) = Track(
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