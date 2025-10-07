package ru.nkyancen.playlistmaker.domain.use_case.track

import ru.nkyancen.playlistmaker.domain.repository.track.TrackRepository

class SearchTracksUseCase(
    private val trackRepository: TrackRepository
) {
    operator fun invoke(expression: String) = trackRepository.searchTracks(expression)
}