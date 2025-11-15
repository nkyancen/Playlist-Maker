package ru.nkyancen.playlistmaker.presentation.medialibrary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.nkyancen.playlistmaker.presentation.medialibrary.model.PlaylistsState

class PlaylistsViewModel: ViewModel() {

    private val playlistsStateLiveData = MutableLiveData<PlaylistsState>(PlaylistsState.Empty)
    fun observePlaylistsState() : LiveData<PlaylistsState> = playlistsStateLiveData
}