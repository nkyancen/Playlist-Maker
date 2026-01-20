package ru.nkyancen.playlistmaker.medialibrary.favorites.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.entity.TrackEntity
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.sources.local.db.dao.FavoritesTrackDao
import ru.nkyancen.playlistmaker.medialibrary.favorites.domain.api.FavoritesRepository
import ru.nkyancen.playlistmaker.search.domain.models.Track

class FavoritesRepositoryImpl(
    private val dao: FavoritesTrackDao,
    private val entityMapper: TrackMapper<TrackEntity>
): FavoritesRepository {

    override suspend fun insertTrackToFavorites(track: Track) {
        dao.insertTrack(
            entityMapper.mapFromDomain(track)
        )
    }

    override suspend fun deleteTrackFromFavorites(track: Track) {
        dao.deleteTrack(
            entityMapper.mapFromDomain(track)
        )
    }

    override fun getFavoritesTracks(): Flow<List<Track>> = flow{
        val tracks = dao.getTracks()

        emit(
            tracks.map {
                entityMapper.mapToDomain(it)
            }
        )
    }

    override fun getFavoritesTracksId(): Flow<List<Long>> = flow{
        val tracksId = dao.getTracksId()

        emit(
            tracksId
        )
    }
}