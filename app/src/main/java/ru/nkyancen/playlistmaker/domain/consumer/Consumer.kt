package ru.nkyancen.playlistmaker.domain.consumer

import ru.nkyancen.playlistmaker.domain.models.Resource
import ru.nkyancen.playlistmaker.domain.models.Track

interface Consumer {
    fun consume(tracks: Resource<List<Track>?>)
}