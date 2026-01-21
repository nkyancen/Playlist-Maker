package ru.nkyancen.playlistmaker.search.domain.use_case

import kotlinx.coroutines.flow.Flow
import ru.nkyancen.playlistmaker.search.domain.api.HistoryRepository
import ru.nkyancen.playlistmaker.search.domain.api.TrackInteractor
import ru.nkyancen.playlistmaker.search.domain.api.TrackSearchRepository
import ru.nkyancen.playlistmaker.search.domain.models.Resource
import ru.nkyancen.playlistmaker.search.domain.models.Track

class TrackInteractorImpl(
    private val trackSearchRepository: TrackSearchRepository,
    private val historyRepository: HistoryRepository
) : TrackInteractor {

    override fun loadHistoryOfPlayedTracks(): Flow<List<Track>> =
        historyRepository.loadTracksFromHistory()

    override fun clearTracksHistory() =
        historyRepository.clearTracksHistory()

    override fun addSelectedTrackToHistory(newItem: Track) =
        historyRepository.addSelectedTrackToHistory(newItem)

    override fun searchTracks(
        expression: String
    ): Flow<Resource<List<Track>>> =
        trackSearchRepository.searchTracks(expression)
}


