package ru.nkyancen.playlistmaker.data.repository

import ru.nkyancen.playlistmaker.data.mappers.TrackDataMapper
import ru.nkyancen.playlistmaker.data.model.TrackSearchRequest
import ru.nkyancen.playlistmaker.data.model.TrackSearchResponse
import ru.nkyancen.playlistmaker.data.sources.RemoteClient
import ru.nkyancen.playlistmaker.domain.models.Resource
import ru.nkyancen.playlistmaker.domain.models.Track
import ru.nkyancen.playlistmaker.domain.repository.track.TrackRepository

class TrackRepositoryImpl(
    private val removeClient: RemoteClient
) : TrackRepository {
    override fun searchTracks(expression: String): Resource<List<Track>?> {
        val response = removeClient.doRequest(TrackSearchRequest(expression))

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

