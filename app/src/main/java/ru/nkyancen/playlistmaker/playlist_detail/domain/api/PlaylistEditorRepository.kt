package ru.nkyancen.playlistmaker.playlist_detail.domain.api

import ru.nkyancen.playlistmaker.playlist_detail.domain.model.PlaylistInfo

interface PlaylistEditorRepository {

    suspend fun savePlaylistUpdate(playlistInfo: PlaylistInfo)
}