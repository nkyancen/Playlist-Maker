package ru.nkyancen.playlistmaker.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.data.player.impl.MediaPlayerRepositoryImpl
import ru.nkyancen.playlistmaker.data.search.dto.TrackData
import ru.nkyancen.playlistmaker.data.search.dto.TrackHistory
import ru.nkyancen.playlistmaker.data.search.impl.HistoryRepositoryImpl
import ru.nkyancen.playlistmaker.data.search.impl.TrackSearchRepositoryImpl
import ru.nkyancen.playlistmaker.data.search.mappers.TrackDataMapper
import ru.nkyancen.playlistmaker.data.search.mappers.TrackHistoryMapper
import ru.nkyancen.playlistmaker.data.settings.impl.NightModeRepositoryImpl
import ru.nkyancen.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import ru.nkyancen.playlistmaker.domain.player.api.MediaPlayerInteractor
import ru.nkyancen.playlistmaker.domain.player.api.MediaPlayerRepository
import ru.nkyancen.playlistmaker.domain.player.use_case.MediaPlayerInteractorImpl
import ru.nkyancen.playlistmaker.domain.search.api.HistoryRepository
import ru.nkyancen.playlistmaker.domain.search.api.TrackInteractor
import ru.nkyancen.playlistmaker.domain.search.api.TrackSearchRepository
import ru.nkyancen.playlistmaker.domain.search.use_case.TrackInteractorImpl
import ru.nkyancen.playlistmaker.domain.settings.api.NightModeInteractor
import ru.nkyancen.playlistmaker.domain.settings.api.NightModeRepository
import ru.nkyancen.playlistmaker.domain.settings.use_case.NightModeInteractorImpl
import ru.nkyancen.playlistmaker.domain.sharing.api.ExternalNavigator
import ru.nkyancen.playlistmaker.domain.sharing.api.SharingInteractor
import ru.nkyancen.playlistmaker.domain.sharing.use_case.SharingInteractorImpl

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

    factory<TrackMapper<TrackHistory>>(named("historyMapper")) {
        TrackHistoryMapper()
    }

    factory<TrackMapper<TrackData>>(named("searchMapper")) {
        TrackDataMapper()
    }

    factory<HistoryRepository> {
        HistoryRepositoryImpl(
            get(named(HISTORY_PREFS_CLIENT)),
            get(named("historyMapper")),
            get()
        )
    }

    factory<TrackSearchRepository> {
        TrackSearchRepositoryImpl(
            get(),
            get(named("searchMapper")))
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
}