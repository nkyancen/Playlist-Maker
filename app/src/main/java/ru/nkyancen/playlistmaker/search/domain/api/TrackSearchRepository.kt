package ru.nkyancen.playlistmaker.search.domain.api

import ru.nkyancen.playlistmaker.search.domain.models.Resource
import ru.nkyancen.playlistmaker.search.domain.models.Track

interface TrackSearchRepository {
    fun searchTracks(expression: String): Resource<List<Track>?>
}