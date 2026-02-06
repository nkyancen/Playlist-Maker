package ru.nkyancen.playlistmaker.playlist_detail.domain.api

import kotlinx.coroutines.flow.Flow
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.model.Playlist
import ru.nkyancen.playlistmaker.search.domain.models.Track

interface PlaylistDetailsRepository {

    fun getAllTracksFromPlaylist(playlist: Playlist): Flow<List<Track>>

    fun getTotalProgressTimeByPlaylist(playlist: Playlist): Flow<Int>

    fun getPlaylistContentByPlainText(playlist: Playlist): Flow<String>
}