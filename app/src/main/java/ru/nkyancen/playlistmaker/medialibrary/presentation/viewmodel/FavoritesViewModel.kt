package ru.nkyancen.playlistmaker.medialibrary.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.nkyancen.playlistmaker.medialibrary.presentation.model.FavoritesState

class FavoritesViewModel: ViewModel() {

    private val favoritesStateLiveData = MutableLiveData<FavoritesState>(FavoritesState.Empty)
    fun observeFavoritesState() : LiveData<FavoritesState> = favoritesStateLiveData
}