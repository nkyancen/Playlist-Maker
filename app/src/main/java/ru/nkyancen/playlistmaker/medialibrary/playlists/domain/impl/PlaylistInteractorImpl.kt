package ru.nkyancen.playlistmaker.medialibrary.playlists.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistInteractor
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistRepository
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.model.Playlist

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
): PlaylistInteractor {
    override suspend fun savePlaylistToStorage(playlist: Playlist) {
        playlistRepository.savePlaylistToStorage(playlist)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> =
        playlistRepository.getAllPlaylists()

    override fun getTracksIdFromPlaylist(playlist: Playlist): List<Long> =
        playlistRepository.getTracksIdFromPlaylist(playlist)

    override suspend fun addTrackIdToPlaylist(trackId: Long, playlistId: Long) {
        playlistRepository.addTrackIdToPlaylist(trackId, playlistId)
    }
}