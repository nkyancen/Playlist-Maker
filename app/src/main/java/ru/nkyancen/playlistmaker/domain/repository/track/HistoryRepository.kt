package ru.nkyancen.playlistmaker.domain.repository.track

import ru.nkyancen.playlistmaker.domain.models.Track

interface HistoryRepository {
    fun saveTracksToHistory(tracks: List<Track>)
    fun loadTracksFromHistory(): List<Track>
    fun addSelectedTrackToHistory(newItem: Track)
}