package ru.nkyancen.playlistmaker.playlist_detail.domain.impl

import ru.nkyancen.playlistmaker.playlist_detail.domain.api.PlaylistEditorInteractor
import ru.nkyancen.playlistmaker.playlist_detail.domain.api.PlaylistEditorRepository
import ru.nkyancen.playlistmaker.playlist_detail.domain.model.PlaylistInfo

class PlaylistEditorInteractorImpl(
    private val editorRepository: PlaylistEditorRepository
) : PlaylistEditorInteractor {
    override suspend fun savePlaylistUpdate(playlistInfo: PlaylistInfo) {
        editorRepository.savePlaylistUpdate(playlistInfo)
    }
}