package ru.nkyancen.playlistmaker.medialibrary.presentation.model

sealed interface FavoritesState {
    object Empty: FavoritesState
}