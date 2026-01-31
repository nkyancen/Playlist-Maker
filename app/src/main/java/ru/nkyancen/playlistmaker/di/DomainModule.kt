package ru.nkyancen.playlistmaker.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.impl.FavoritesRepositoryImpl
import ru.nkyancen.playlistmaker.medialibrary.favorites.domain.api.FavoritesInteractor
import ru.nkyancen.playlistmaker.medialibrary.favorites.domain.api.FavoritesRepository
import ru.nkyancen.playlistmaker.medialibrary.favorites.domain.impl.FavoritesInteractorImpl
import ru.nkyancen.playlistmaker.medialibrary.playlists.data.impl.ExternalStorageRepositoryImpl
import ru.nkyancen.playlistmaker.medialibrary.playlists.data.impl.PlaylistRepositoryImpl
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.ExternalStorageInteractor
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.ExternalStorageRepository
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistInteractor
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistRepository
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.impl.ExternalStorageInteractorImpl
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.impl.PlaylistInteractorImpl
import ru.nkyancen.playlistmaker.player.data.impl.MediaPlayerRepositoryImpl
import ru.nkyancen.playlistmaker.player.domain.api.MediaPlayerInteractor
import ru.nkyancen.playlistmaker.player.domain.api.MediaPlayerRepository
import ru.nkyancen.playlistmaker.player.domain.use_case.MediaPlayerInteractorImpl
import ru.nkyancen.playlistmaker.search.data.impl.HistoryRepositoryImpl
import ru.nkyancen.playlistmaker.search.data.impl.TrackSearchRepositoryImpl
import ru.nkyancen.playlistmaker.search.domain.api.HistoryRepository
import ru.nkyancen.playlistmaker.search.domain.api.TrackInteractor
import ru.nkyancen.playlistmaker.search.domain.api.TrackSearchRepository
import ru.nkyancen.playlistmaker.search.domain.use_case.TrackInteractorImpl
import ru.nkyancen.playlistmaker.settings.data.impl.ExternalNavigatorImpl
import ru.nkyancen.playlistmaker.settings.data.impl.NightModeRepositoryImpl
import ru.nkyancen.playlistmaker.settings.domain.api.ExternalNavigator
import ru.nkyancen.playlistmaker.settings.domain.api.NightModeInteractor
import ru.nkyancen.playlistmaker.settings.domain.api.NightModeRepository
import ru.nkyancen.playlistmaker.settings.domain.api.SharingInteractor
import ru.nkyancen.playlistmaker.settings.domain.use_case.NightModeInteractorImpl
import ru.nkyancen.playlistmaker.settings.domain.use_case.SharingInteractorImpl

val repositoryModule = module {
    factory<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    factory<NightModeRepository> {
        NightModeRepositoryImpl(
            get(named(NIGHT_MODE_PREFS_CLIENT)),
            androidContext())
    }

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    factory<HistoryRepository> {
        HistoryRepositoryImpl(
            get(named(HISTORY_PREFS_CLIENT)),
            get(named(HISTORY_MAPPER)),
            get()
        )
    }

    factory<TrackSearchRepository> {
        TrackSearchRepositoryImpl(
            get(),
            get(named(SEARCH_MAPPER)))
    }

    factory<FavoritesRepository> {
        FavoritesRepositoryImpl(
            get(),
            get(named(ENTITY_MAPPER))
        )
    }

    factory<PlaylistRepository> {
        PlaylistRepositoryImpl(
            get(),
            get(named(PLAYLIST_ENTITY_MAPPER)),
            get()
        )
    }

    factory<ExternalStorageRepository> {
        ExternalStorageRepositoryImpl(
            androidContext()
        )
    }
}

val interactorModule = module {
    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    factory<NightModeInteractor> {
        NightModeInteractorImpl(get())
    }

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }

    factory<TrackInteractor> {
        TrackInteractorImpl(get(), get())
    }

    factory<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }

    factory<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    factory<ExternalStorageInteractor> {
        ExternalStorageInteractorImpl(get())
    }
}
