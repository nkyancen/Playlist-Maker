package ru.nkyancen.playlistmaker.domain.search.api

import ru.nkyancen.playlistmaker.domain.search.consumer.Consumer
import ru.nkyancen.playlistmaker.domain.search.models.Track

interface TrackInteractor {
    fun searchTracks(expression: String, consumer: Consumer)

    fun clearTracksHistory()

    fun loadHistoryOfPlayedTracks(): List<Track>

    fun addSelectedTrackToHistory(newItem: Track)
}
