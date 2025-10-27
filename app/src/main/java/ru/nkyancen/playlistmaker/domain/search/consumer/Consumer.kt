package ru.nkyancen.playlistmaker.domain.search.consumer

import ru.nkyancen.playlistmaker.domain.search.models.Resource
import ru.nkyancen.playlistmaker.domain.search.models.Track

interface Consumer {
    fun consume(tracksResponse: Resource<List<Track>?>)
}