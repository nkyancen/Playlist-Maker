package ru.nkyancen.playlistmaker.data.repository

import ru.nkyancen.playlistmaker.data.mappers.TrackDataMapper
import ru.nkyancen.playlistmaker.data.model.TrackSearchRequest
import ru.nkyancen.playlistmaker.data.model.TrackSearchResponse
import ru.nkyancen.playlistmaker.data.sources.remote.RemoteClient
import ru.nkyancen.playlistmaker.domain.models.Resource
import ru.nkyancen.playlistmaker.domain.models.Track
import ru.nkyancen.playlistmaker.domain.repository.track.TrackRepository

class TrackRepositoryImpl(
    private val remoteClient: RemoteClient
) : TrackRepository {
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

