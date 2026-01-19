package ru.nkyancen.playlistmaker.medialibrary.favorites.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.entity.TrackEntity
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.sources.local.db.AppDatabase
import ru.nkyancen.playlistmaker.medialibrary.favorites.domain.api.FavoritesRepository
import ru.nkyancen.playlistmaker.search.domain.models.Track

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val entityMapper: TrackMapper<TrackEntity>
): FavoritesRepository {

    override suspend fun insertTrackToFavorites(track: Track) {
        appDatabase.favoritesDao().insertTrack(
            entityMapper.mapFromDomain(track)
        )
    }

    override suspend fun deleteTrackFromFavorites(track: Track) {
        appDatabase.favoritesDao().deleteTrack(
            entityMapper.mapFromDomain(track)
        )
    }

    override fun getFavoritesTracks(): Flow<List<Track>> = flow{
        val tracks = appDatabase.favoritesDao().getTracks()

        emit(
            tracks.map {
                entityMapper.mapToDomain(it).apply {
                    isFavorite = true
                }
            }
        )
    }
}