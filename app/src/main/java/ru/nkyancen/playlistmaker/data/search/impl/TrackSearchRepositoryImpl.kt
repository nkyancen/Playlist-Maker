package ru.nkyancen.playlistmaker.data.search.impl

import ru.nkyancen.playlistmaker.data.search.dto.TrackSearchRequest
import ru.nkyancen.playlistmaker.data.search.dto.TrackSearchResponse
import ru.nkyancen.playlistmaker.data.search.mappers.TrackDataMapper
import ru.nkyancen.playlistmaker.data.search.sources.remote.RemoteClient
import ru.nkyancen.playlistmaker.domain.search.api.TrackSearchRepository
import ru.nkyancen.playlistmaker.domain.search.models.Resource
import ru.nkyancen.playlistmaker.domain.search.models.Track

class TrackSearchRepositoryImpl(
    private val remoteClient: RemoteClient
) : TrackSearchRepository {
    override fun searchTracks(expression: String): Resource<List<Track>?> {
        val response = remoteClient.doRequest(TrackSearchRequest(expression))

        return if (response.isSuccess()) {
            Resource(
                expression,
                TrackDataMapper().mapListToDomain(
                    (response as TrackSearchResponse).tracks
                )
            )
        } else {
            Resource(
                expression,
                null
            )
        }
    }
}

