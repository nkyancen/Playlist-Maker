package ru.nkyancen.playlistmaker.domain.repository.track

import ru.nkyancen.playlistmaker.domain.consumer.Consumer
import ru.nkyancen.playlistmaker.domain.models.Track

interface TrackInteractor {
    fun searchTracks(expression: String, consumer: Consumer)

    fun savePlayedTracksToHistory(tracks: List<Track>)

    fun loadHistoryOfPlayedTracks(): List<Track>

    fun addSelectedTrackToHistory(newItem: Track)
}
