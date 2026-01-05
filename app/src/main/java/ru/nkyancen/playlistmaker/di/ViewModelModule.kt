package ru.nkyancen.playlistmaker.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.medialibrary.presentation.viewmodel.FavoritesViewModel
import ru.nkyancen.playlistmaker.medialibrary.presentation.viewmodel.PlaylistsViewModel
import ru.nkyancen.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import ru.nkyancen.playlistmaker.search.presentation.mappers.TrackItemMapper
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem
import ru.nkyancen.playlistmaker.search.presentation.viewmodel.SearchViewModel
import ru.nkyancen.playlistmaker.settings.presentation.viewmodel.SettingsViewModel

val viewModelModule = module {


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