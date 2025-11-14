package ru.nkyancen.playlistmaker.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.presentation.main.viewmodel.MainViewModel
import ru.nkyancen.playlistmaker.presentation.medialibrary.viewmodel.FavoritesViewModel
import ru.nkyancen.playlistmaker.presentation.medialibrary.viewmodel.PlaylistsViewModel
import ru.nkyancen.playlistmaker.presentation.player.viewmodel.PlayerViewModel
import ru.nkyancen.playlistmaker.presentation.search.mappers.TrackItemMapper
import ru.nkyancen.playlistmaker.presentation.search.model.TrackItem
import ru.nkyancen.playlistmaker.presentation.search.viewmodel.SearchViewModel
import ru.nkyancen.playlistmaker.presentation.settings.viewmodel.SettingsViewModel

val viewModelModule = module {
    viewModel {
        MainViewModel()
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel { (url: String) ->
        PlayerViewModel(url, get())
    }

    single<TrackMapper<TrackItem>> (named("itemMapper")) {
        TrackItemMapper()
    }

    viewModel {
        SearchViewModel(get(), get(named("itemMapper")))
    }

    viewModel {
        FavoritesViewModel()
    }

    viewModel {
        PlaylistsViewModel()
    }
}