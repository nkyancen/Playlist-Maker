package ru.nkyancen.playlistmaker.domain.search.api

import ru.nkyancen.playlistmaker.domain.search.models.Track

interface HistoryRepository {
    fun clearTracksHistory()
    fun loadTracksFromHistory(): List<Track>
    fun addSelectedTrackToHistory(newItem: Track)
}