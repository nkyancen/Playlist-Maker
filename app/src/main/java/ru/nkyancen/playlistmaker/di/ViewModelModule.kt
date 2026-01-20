package ru.nkyancen.playlistmaker.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.nkyancen.playlistmaker.medialibrary.favorites.presentation.viewmodel.FavoritesViewModel
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.viewmodel.PlaylistsViewModel
import ru.nkyancen.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import ru.nkyancen.playlistmaker.search.presentation.viewmodel.SearchViewModel
import ru.nkyancen.playlistmaker.settings.presentation.viewmodel.SettingsViewModel


val viewModelModule = module {

    viewModel {
        SettingsViewModel(
            get(),
            get()
        )
    }

    viewModel { (id: Long, url: String) ->
        PlayerViewModel(
            id,
            url,
            get(),
            get(),
            get(named(ITEM_MAPPER))
        )
    }

    viewModel {
        SearchViewModel(
            get(),
            get(named(ITEM_MAPPER))
        )
    }

    viewModel {
        FavoritesViewModel(
            get(),
            get(named(ITEM_MAPPER))
        )
    }

    viewModel {
        PlaylistsViewModel()
    }
}