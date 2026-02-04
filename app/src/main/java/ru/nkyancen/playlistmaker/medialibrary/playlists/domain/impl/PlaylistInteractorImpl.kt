package ru.nkyancen.playlistmaker.medialibrary.playlists.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistInteractor
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistRepository
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.model.Playlist
import ru.nkyancen.playlistmaker.search.domain.models.Track

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
): PlaylistInteractor {
    override suspend fun savePlaylistToStorage(playlist: Playlist) {
        playlistRepository.savePlaylistToStorage(playlist)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> =
        playlistRepository.getAllPlaylists()

    override fun getPlaylistById(playlistId: Long): Flow<Playlist> =
        playlistRepository.getPlaylistById(playlistId)

    override fun getTracksIdFromPlaylist(playlist: Playlist): List<Long> =
        playlistRepository.getTracksIdList(playlist)

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.addTrackToPlaylist(track, playlist)
    }

    override suspend fun deleteTrackFromPlaylist(
        trackId: Long,
        playlist: Playlist
    ) {
        playlistRepository.deleteTrackFromPlaylist(trackId, playlist)
    }

    override suspend fun deletePlaylistById(playlistId: Long) {
        playlistRepository.deletePlaylistById(playlistId)
    }
}