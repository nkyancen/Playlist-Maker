package ru.nkyancen.playlistmaker.medialibrary.favorites.domain.api

import kotlinx.coroutines.flow.Flow
import ru.nkyancen.playlistmaker.search.domain.models.Track

interface FavoritesInteractor {
    suspend fun insertTrackToFavorites(track: Track)

    suspend fun deleteTrackFromFavorites(track: Track)

    fun getFavoritesTracks() : Flow<List<Track>>

    fun getFavoriteTracksId(): Flow<List<Long>>
}