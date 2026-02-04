package ru.nkyancen.playlistmaker.playlist_detail.data.impl

import ru.nkyancen.playlistmaker.medialibrary.playlists.data.sources.local.db.dao.PlaylistDao
import ru.nkyancen.playlistmaker.playlist_detail.domain.api.PlaylistEditorRepository
import ru.nkyancen.playlistmaker.playlist_detail.domain.model.PlaylistInfo

class PlaylistEditorRepositoryImpl(
    private var playlistDao: PlaylistDao
) : PlaylistEditorRepository {

    override suspend fun savePlaylistUpdate(playlistInfo: PlaylistInfo) {

        playlistDao.updatePlaylistInfo(
            playlistInfo.id,
            playlistInfo.title,
            playlistInfo.coverName,
            playlistInfo.description
        )
    }
}