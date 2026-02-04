package ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api

import kotlinx.coroutines.flow.Flow
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.model.Playlist
import ru.nkyancen.playlistmaker.search.domain.models.Track

interface PlaylistRepository {
    suspend fun savePlaylistToStorage(playlist: Playlist)

    fun getAllPlaylists(): Flow<List<Playlist>>

    fun getTracksIdList(playlist: Playlist): List<Long>

    fun getPlaylistById(playlistId: Long): Flow<Playlist>

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

    suspend fun deleteTrackFromPlaylist(trackId: Long, playlist: Playlist)

    suspend fun deletePlaylistById(playlistId: Long)

}