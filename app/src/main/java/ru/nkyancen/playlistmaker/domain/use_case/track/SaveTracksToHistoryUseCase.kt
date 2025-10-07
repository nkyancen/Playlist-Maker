package ru.nkyancen.playlistmaker.domain.use_case.track

import ru.nkyancen.playlistmaker.domain.models.Track
import ru.nkyancen.playlistmaker.domain.repository.track.HistoryRepository

class SaveTracksToHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    operator fun invoke(tracks: List<Track>) {
        historyRepository.saveTracksToHistory(tracks)
    }
}