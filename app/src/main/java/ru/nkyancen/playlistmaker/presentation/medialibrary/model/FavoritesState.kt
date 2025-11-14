package ru.nkyancen.playlistmaker.presentation.medialibrary.model

sealed interface FavoritesState {
    object Empty: FavoritesState
}