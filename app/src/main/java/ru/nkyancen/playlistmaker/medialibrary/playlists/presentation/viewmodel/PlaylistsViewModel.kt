package ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nkyancen.playlistmaker.core.utils.PlaylistMapper
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistCoverInteractor
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistInteractor
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistsState

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlistMapper: PlaylistMapper<PlaylistItem>,
    private val playlistCoverInteractor: PlaylistCoverInteractor
) : ViewModel() {

    private val playlistsStateLiveData = MutableLiveData<PlaylistsState>(PlaylistsState.Loading)
    fun observePlaylistsState(): LiveData<PlaylistsState> = playlistsStateLiveData

    fun showPlaylists() {
        renderState(PlaylistsState.Loading)

        viewModelScope.launch {
            playlistInteractor
                .getAllPlaylists()
                .collect { playlists ->
                    processResult(
                        playlistMapper.mapListFromDomain(playlists)
                    )
                }
        }
    }

    fun getUriForCover(coverName: String): Uri? =
        if (coverName.isNotEmpty()) {
            playlistCoverInteractor.loadImageFromStorage(coverName)
        } else {
            null
        }


private fun processResult(playlists: List<PlaylistItem>) {
    if (playlists.isEmpty()) {
        renderState(PlaylistsState.Empty)
    } else {
        renderState(PlaylistsState.Content(playlists))
    }
}

private fun renderState(state: PlaylistsState) {
    playlistsStateLiveData.postValue(state)
}
}