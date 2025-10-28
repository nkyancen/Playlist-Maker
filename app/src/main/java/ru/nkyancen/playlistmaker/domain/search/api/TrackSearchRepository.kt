package ru.nkyancen.playlistmaker.domain.search.api

import ru.nkyancen.playlistmaker.domain.search.models.Resource
import ru.nkyancen.playlistmaker.domain.search.models.Track

interface TrackSearchRepository {
    fun searchTracks(expression: String): Resource<List<Track>?>
}