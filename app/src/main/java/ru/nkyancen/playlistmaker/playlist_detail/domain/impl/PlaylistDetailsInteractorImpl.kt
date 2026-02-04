package ru.nkyancen.playlistmaker.playlist_detail.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.model.Playlist
import ru.nkyancen.playlistmaker.playlist_detail.domain.api.PlaylistDetailInteractor
import ru.nkyancen.playlistmaker.playlist_detail.domain.api.PlaylistDetailsRepository
import ru.nkyancen.playlistmaker.search.domain.models.Track

class PlaylistDetailsInteractorImpl(
    private val playlistDetailsRepository: PlaylistDetailsRepository
) : PlaylistDetailInteractor {
    override fun getAllTracksFromPlaylist(playlist: Playlist): Flow<List<Track>> =
        playlistDetailsRepository.getAllTracksFromPlaylist(playlist)

    override fun getTotalProgressTimeByPlaylist(playlist: Playlist): Flow<Int> =
        playlistDetailsRepository.getTotalProgressTimeByPlaylist(playlist)

    override fun getPlaylistContent(playlist: Playlist): Flow<String> =
        playlistDetailsRepository.getPlaylistContentByPlainText(playlist)
}