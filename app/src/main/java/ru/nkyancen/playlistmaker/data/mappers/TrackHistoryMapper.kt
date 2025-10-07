package ru.nkyancen.playlistmaker.data.mappers

import ru.nkyancen.playlistmaker.data.model.TrackHistory
import ru.nkyancen.playlistmaker.domain.models.Track

class TrackHistoryMapper {
    fun mapToHistory(model: Track) =
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

    fun mapListToHistory(models: List<Track>) = models.map {
        mapToHistory(it)
    }

    fun mapToDomain(item: TrackHistory) = Track(
        id = item.id,
        trackName = item.trackName,
        artistName = item.artistName,
        trackTime = item.trackTime,
        albumPoster = item.albumPoster,
        albumName = item.albumName,
        releaseYear = item.releaseYear,
        genre = item.genre,
        country = item.country,
        preview = item.preview
    )

    fun mapListToDomain(items: List<TrackHistory>) = items.map {
        mapToDomain(it)
    }

}