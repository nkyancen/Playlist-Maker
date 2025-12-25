package ru.nkyancen.playlistmaker.search.data.impl

import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.search.data.dto.TrackData
import ru.nkyancen.playlistmaker.search.data.dto.TrackSearchRequest
import ru.nkyancen.playlistmaker.search.data.dto.TrackSearchResponse
import ru.nkyancen.playlistmaker.search.data.sources.remote.RemoteClient
import ru.nkyancen.playlistmaker.search.domain.api.TrackSearchRepository
import ru.nkyancen.playlistmaker.search.domain.models.Resource
import ru.nkyancen.playlistmaker.search.domain.models.Track

class TrackSearchRepositoryImpl(
    private val remoteClient: RemoteClient,
    private val dataMapper: TrackMapper<TrackData>
) : TrackSearchRepository {
    override fun searchTracks(expression: String): Resource<List<Track>?> {
        val response = remoteClient.doRequest(TrackSearchRequest(expression))

        return if (response.isSuccess()) {
            Resource(
                expression,
                dataMapper.mapListToDomain(
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

