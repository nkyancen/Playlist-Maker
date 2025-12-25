package ru.nkyancen.playlistmaker.search.domain.api

import ru.nkyancen.playlistmaker.search.domain.models.Track

interface HistoryRepository {
    fun clearTracksHistory()
    fun loadTracksFromHistory(): List<Track>
    fun addSelectedTrackToHistory(newItem: Track)
}