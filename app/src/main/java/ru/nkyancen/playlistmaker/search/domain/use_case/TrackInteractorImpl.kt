package ru.nkyancen.playlistmaker.search.domain.use_case

import ru.nkyancen.playlistmaker.search.domain.api.HistoryRepository
import ru.nkyancen.playlistmaker.search.domain.api.TrackInteractor
import ru.nkyancen.playlistmaker.search.domain.api.TrackSearchRepository
import ru.nkyancen.playlistmaker.search.domain.api.Consumer
import ru.nkyancen.playlistmaker.search.domain.models.Track
import java.util.concurrent.Executors

class TrackInteractorImpl(
    private val trackSearchRepository: TrackSearchRepository,
    private val historyRepository: HistoryRepository
) : TrackInteractor {

    override fun loadHistoryOfPlayedTracks(): List<Track> =
        historyRepository.loadTracksFromHistory()

    override fun clearTracksHistory() =
        historyRepository.clearTracksHistory()

    override fun addSelectedTrackToHistory(newItem: Track) =
        historyRepository.addSelectedTrackToHistory(newItem)

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(
        expression: String,
        consumer: Consumer
    ) {
        executor.execute {
            consumer.consume(
                trackSearchRepository.searchTracks(expression)
            )
        }
    }

}