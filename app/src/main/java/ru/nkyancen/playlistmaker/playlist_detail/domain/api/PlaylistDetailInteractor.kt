package ru.nkyancen.playlistmaker.playlist_detail.domain.api

import kotlinx.coroutines.flow.Flow
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.model.Playlist
import ru.nkyancen.playlistmaker.search.domain.models.Track

interface PlaylistDetailInteractor {

    fun getAllTracksFromPlaylist(playlist: Playlist): Flow<List<Track>>

    fun getTotalProgressTimeByPlaylist(playlist: Playlist): Flow<Int>

    fun getPlaylistContent(playlist: Playlist): Flow<String>
}