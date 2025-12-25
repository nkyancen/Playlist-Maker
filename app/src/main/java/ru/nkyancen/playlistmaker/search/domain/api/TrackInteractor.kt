package ru.nkyancen.playlistmaker.search.domain.api

import ru.nkyancen.playlistmaker.search.domain.models.Resource
import ru.nkyancen.playlistmaker.search.domain.models.Track

interface TrackInteractor {
    fun searchTracks(expression: String, consumer: Consumer)

    fun clearTracksHistory()

    fun loadHistoryOfPlayedTracks(): List<Track>

    fun addSelectedTrackToHistory(newItem: Track)
}

interface Consumer {
    fun consume(tracksResponse: Resource<List<Track>?>)
}