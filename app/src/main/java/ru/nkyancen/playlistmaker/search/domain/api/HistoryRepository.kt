package ru.nkyancen.playlistmaker.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.nkyancen.playlistmaker.search.domain.models.Track

interface HistoryRepository {
    fun clearTracksHistory()
    fun loadTracksFromHistory(): Flow<List<Track>>
    fun addSelectedTrackToHistory(newItem: Track)
}