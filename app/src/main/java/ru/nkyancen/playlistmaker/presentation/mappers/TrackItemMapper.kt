package ru.nkyancen.playlistmaker.presentation.mappers

import ru.nkyancen.playlistmaker.domain.models.Track
import ru.nkyancen.playlistmaker.presentation.model.TrackItem

class TrackItemMapper {
    fun mapToItem(model: Track) = TrackItem(
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

    fun mapListToItem(models: List<Track>) = models.map {
        mapToItem(it)
    }

    fun mapToDomain(item: TrackItem) = Track(
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

    fun mapListToDomain(items: List<TrackItem>) = items.map {
        mapToDomain(it)
    }
}