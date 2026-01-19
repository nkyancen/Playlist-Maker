package ru.nkyancen.playlistmaker.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.entity.TrackEntity
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.impl.FavoritesRepositoryImpl
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.mappers.TrackEntityMapper
import ru.nkyancen.playlistmaker.medialibrary.favorites.domain.api.FavoritesInteractor
import ru.nkyancen.playlistmaker.medialibrary.favorites.domain.api.FavoritesRepository
import ru.nkyancen.playlistmaker.medialibrary.favorites.domain.impl.FavoritesInteractorImpl
import ru.nkyancen.playlistmaker.player.data.impl.MediaPlayerRepositoryImpl
import ru.nkyancen.playlistmaker.player.domain.api.MediaPlayerInteractor
import ru.nkyancen.playlistmaker.player.domain.api.MediaPlayerRepository
import ru.nkyancen.playlistmaker.player.domain.use_case.MediaPlayerInteractorImpl
import ru.nkyancen.playlistmaker.search.data.dto.TrackData
import ru.nkyancen.playlistmaker.search.data.dto.TrackHistory
import ru.nkyancen.playlistmaker.search.data.impl.HistoryRepositoryImpl
import ru.nkyancen.playlistmaker.search.data.impl.TrackSearchRepositoryImpl
import ru.nkyancen.playlistmaker.search.data.mappers.TrackDataMapper
import ru.nkyancen.playlistmaker.search.data.mappers.TrackHistoryMapper
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

    single<TrackMapper<TrackHistory>>(named("historyMapper")) {
        TrackHistoryMapper()
    }

    factory<HistoryRepository> {
        HistoryRepositoryImpl(
            get(named(HISTORY_PREFS_CLIENT)),
            get(named("historyMapper")),
            get(),
            get()
        )
    }

    single<TrackMapper<TrackData>>(named("searchMapper")) {
        TrackDataMapper()
    }

    factory<TrackSearchRepository> {
        TrackSearchRepositoryImpl(
            get(),
            get(named("searchMapper")),
            get())
    }

    single<TrackMapper<TrackEntity>>(named("entityMapper")) {
        TrackEntityMapper()
    }

    factory<FavoritesRepository> {
        FavoritesRepositoryImpl(
            get(),
            get(named("entityMapper"))
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
}