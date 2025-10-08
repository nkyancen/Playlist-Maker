package ru.nkyancen.playlistmaker.domain.repository.track

import ru.nkyancen.playlistmaker.domain.models.Resource
import ru.nkyancen.playlistmaker.domain.models.Track

interface TrackRepository {
    fun searchTracks(expression: String): Resource<List<Track>?>
}