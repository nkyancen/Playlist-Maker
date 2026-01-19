package ru.nkyancen.playlistmaker.medialibrary.favorites.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.medialibrary.favorites.domain.api.FavoritesInteractor
import ru.nkyancen.playlistmaker.medialibrary.favorites.presentation.model.FavoritesState
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor,
    private val favoritesMapper: TrackMapper<TrackItem>
) : ViewModel() {

    private val favoritesStateLiveData = MutableLiveData<FavoritesState>(FavoritesState.Loading)
    fun observeFavoritesState(): LiveData<FavoritesState> = favoritesStateLiveData

    fun showFavorites() {
        renderState(FavoritesState.Loading)

        viewModelScope.launch {
            favoritesInteractor
                .getFavoritesTracks()
                .collect { favorites ->
                    processResult(
                        favoritesMapper.mapListFromDomain(favorites)
                    )
                }
        }
    }

    private fun processResult(favorites: List<TrackItem>) {
        if (favorites.isEmpty()) {
            renderState(FavoritesState.Empty)
        } else {
            renderState(
                FavoritesState.Content(favorites)
            )
        }
    }

    private fun renderState(state: FavoritesState) {
        favoritesStateLiveData.postValue(state)
    }
}