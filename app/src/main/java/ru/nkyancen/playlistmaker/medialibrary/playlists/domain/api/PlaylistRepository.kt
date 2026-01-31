package ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api

import kotlinx.coroutines.flow.Flow
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.model.Playlist

interface PlaylistRepository {
    suspend fun savePlaylistToStorage(playlist: Playlist)

    fun getAllPlaylists(): Flow<List<Playlist>>

    fun getTracksIdFromPlaylist(playlist: Playlist): List<Long>

    suspend fun addTrackIdToPlaylist(trackId: Long, playlistId: Long)
}