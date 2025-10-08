package ru.nkyancen.playlistmaker.domain.use_case

import ru.nkyancen.playlistmaker.domain.consumer.Consumer
import ru.nkyancen.playlistmaker.domain.models.Track
import ru.nkyancen.playlistmaker.domain.repository.track.HistoryRepository
import ru.nkyancen.playlistmaker.domain.repository.track.TrackInteractor
import ru.nkyancen.playlistmaker.domain.repository.track.TrackRepository
import ru.nkyancen.playlistmaker.domain.use_case.track.AddSelectedTrackToHistoryUseCase
import ru.nkyancen.playlistmaker.domain.use_case.track.LoadTracksFromHistoryUseCase
import ru.nkyancen.playlistmaker.domain.use_case.track.SaveTracksToHistoryUseCase
import ru.nkyancen.playlistmaker.domain.use_case.track.SearchTracksUseCase
import java.util.concurrent.Executors

class TrackInteractorImpl(
    private val trackRepository: TrackRepository,
    private val historyRepository: HistoryRepository
) : TrackInteractor {

    override fun loadHistoryOfPlayedTracks(): List<Track> {
        val loadUseCase = LoadTracksFromHistoryUseCase(historyRepository)
        return loadUseCase()
    }

    override fun savePlayedTracksToHistory(tracks: List<Track>) {
        val saveUseCase = SaveTracksToHistoryUseCase(historyRepository)
        saveUseCase(tracks)
    }

    override fun addSelectedTrackToHistory(newItem: Track) {
        val addUseCase = AddSelectedTrackToHistoryUseCase(historyRepository)
        addUseCase(newItem)
    }

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(
        expression: String,
        consumer: Consumer
    ) {
        val searchUseCase = SearchTracksUseCase(trackRepository)

        executor.execute {
            consumer.consume(searchUseCase(expression))
        }
    }

}