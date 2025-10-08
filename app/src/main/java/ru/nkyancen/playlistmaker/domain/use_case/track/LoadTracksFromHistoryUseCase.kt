package ru.nkyancen.playlistmaker.domain.use_case.track

import ru.nkyancen.playlistmaker.domain.repository.track.HistoryRepository

class LoadTracksFromHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    operator fun invoke() = historyRepository.loadTracksFromHistory()
}