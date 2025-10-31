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
    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    single<NightModeRepository> {
        NightModeRepositoryImpl(
            get(named("nightModePrefsClient")),
            androidContext())
    }

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    single<TrackMapper<TrackHistory>>(named("historyMapper")) {
        TrackHistoryMapper()
    }

    single<TrackMapper<TrackData>>(named("searchMapper")) {
        TrackDataMapper()
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(
            get(named("historyPrefsClient")),
            get(named("historyMapper")),
            get()
        )
    }

    single<TrackSearchRepository> {
        TrackSearchRepositoryImpl(
            get(),
            get(named("searchMapper")))
    }
}

val interactorModule = module {
    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<NightModeInteractor> {
        NightModeInteractorImpl(get())
    }

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }

    single<TrackInteractor> {
        TrackInteractorImpl(get(), get())
    }
}