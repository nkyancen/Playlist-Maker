package ru.nkyancen.playlistmaker.domain.search.use_case

import ru.nkyancen.playlistmaker.domain.search.api.HistoryRepository
import ru.nkyancen.playlistmaker.domain.search.api.TrackInteractor
import ru.nkyancen.playlistmaker.domain.search.api.TrackSearchRepository
import ru.nkyancen.playlistmaker.domain.search.consumer.Consumer
import ru.nkyancen.playlistmaker.domain.search.models.Track
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