package ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api

import kotlinx.coroutines.flow.Flow
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.model.Playlist
import ru.nkyancen.playlistmaker.search.domain.models.Track

interface PlaylistInteractor {
    fun savePlaylistToStorage(playlist: Playlist): Flow<Boolean>

    fun getAllPlaylists(): Flow<List<Playlist>>

    fun getTracksIdFromPlaylist(playlist: Playlist): List<Long>

    fun getPlaylistById(playlistId: Long): Flow<Playlist>

    fun addTrackToPlaylist(track: Track, playlist: Playlist): Flow<Boolean>

    fun deleteTrackFromPlaylist(trackId: Long, playlist: Playlist): Flow<Boolean>

    fun deletePlaylistById(playlistId: Long): Flow<Boolean>

    fun savePlaylistUpdate(playlistInfo: Playlist): Flow<Boolean>
}