package ru.nkyancen.playlistmaker.search.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.sources.local.db.AppDatabase
import ru.nkyancen.playlistmaker.search.data.dto.TrackData
import ru.nkyancen.playlistmaker.search.data.dto.TrackSearchRequest
import ru.nkyancen.playlistmaker.search.data.dto.TrackSearchResponse
import ru.nkyancen.playlistmaker.search.data.sources.remote.RemoteClient
import ru.nkyancen.playlistmaker.search.domain.api.TrackSearchRepository
import ru.nkyancen.playlistmaker.search.domain.models.Resource
import ru.nkyancen.playlistmaker.search.domain.models.Track

class TrackSearchRepositoryImpl(
    private val remoteClient: RemoteClient,
    private val dataMapper: TrackMapper<TrackData>,
    private val appDatabase: AppDatabase
) : TrackSearchRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = remoteClient.doRequest(TrackSearchRequest(expression))

        val tracksId = appDatabase.favoritesDao().getTracksId()

        val tracks = (response as TrackSearchResponse).tracks.map { dto ->
            dataMapper.mapToDomain(dto)
                .apply {
                    isFavorite = dto.id in tracksId
                }
        }

        emit(
            Resource(
                expression,
                tracks
            )
        )
    }
}

