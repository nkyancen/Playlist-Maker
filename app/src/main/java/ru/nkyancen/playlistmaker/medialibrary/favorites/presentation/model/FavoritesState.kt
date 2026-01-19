package ru.nkyancen.playlistmaker.medialibrary.favorites.presentation.model

import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem

sealed interface FavoritesState {
    object Empty: FavoritesState

    object Loading: FavoritesState

    data class Content(
        val favorites: List<TrackItem>
    ) : FavoritesState
}