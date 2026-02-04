package ru.nkyancen.playlistmaker.medialibrary.favorites.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.nkyancen.playlistmaker.medialibrary.favorites.domain.api.FavoritesInteractor
import ru.nkyancen.playlistmaker.medialibrary.favorites.domain.api.FavoritesRepository
import ru.nkyancen.playlistmaker.search.domain.models.Track

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
) : FavoritesInteractor {
    override suspend fun insertTrackToFavorites(track: Track) {
        favoritesRepository.insertTrackToFavorites(track)
    }

    override suspend fun deleteTrackFromFavorites(trackId: Long) {
        favoritesRepository.deleteTrackFromFavorites(trackId)
    }

    override fun getFavoritesTracks(): Flow<List<Track>> = favoritesRepository.getFavoritesTracks()

    override fun getFavoriteTracksId(): Flow<List<Long>> =
        favoritesRepository.getFavoritesTracksId()
}