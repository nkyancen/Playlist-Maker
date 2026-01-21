package ru.nkyancen.playlistmaker.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.nkyancen.playlistmaker.search.domain.models.Resource
import ru.nkyancen.playlistmaker.search.domain.models.Track

interface TrackInteractor {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>

    fun clearTracksHistory()

    fun loadHistoryOfPlayedTracks(): Flow<List<Track>>

    fun addSelectedTrackToHistory(newItem: Track)
}
