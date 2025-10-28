package ru.nkyancen.playlistmaker.core.creator

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.nkyancen.playlistmaker.core.App
import ru.nkyancen.playlistmaker.data.player.impl.MediaPlayerRepositoryImpl
import ru.nkyancen.playlistmaker.data.search.impl.HistoryRepositoryImpl
import ru.nkyancen.playlistmaker.data.search.impl.TrackSearchRepositoryImpl
import ru.nkyancen.playlistmaker.data.search.sources.local.prefs.HistoryPrefsClient
import ru.nkyancen.playlistmaker.data.search.sources.local.prefs.LocalPrefsClient
import ru.nkyancen.playlistmaker.data.search.sources.local.prefs.NightModePrefsClient
import ru.nkyancen.playlistmaker.data.search.sources.remote.RemoteClient
import ru.nkyancen.playlistmaker.data.search.sources.remote.RetrofitClient
import ru.nkyancen.playlistmaker.data.settings.impl.NightModeRepositoryImpl
import ru.nkyancen.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import ru.nkyancen.playlistmaker.domain.player.api.MediaPlayerInteractor
import ru.nkyancen.playlistmaker.domain.player.api.MediaPlayerRepository
import ru.nkyancen.playlistmaker.domain.player.use_case.MediaPlayerInteractorImpl
import ru.nkyancen.playlistmaker.domain.search.api.HistoryRepository
import ru.nkyancen.playlistmaker.domain.search.api.TrackInteractor
import ru.nkyancen.playlistmaker.domain.search.api.TrackSearchRepository
import ru.nkyancen.playlistmaker.domain.search.use_case.TrackInteractorImpl
import ru.nkyancen.playlistmaker.domain.settings.api.NightModeRepository
import ru.nkyancen.playlistmaker.domain.settings.use_case.NightModeInteractorImpl
import ru.nkyancen.playlistmaker.domain.sharing.api.ExternalNavigator
import ru.nkyancen.playlistmaker.domain.sharing.api.SharingInteractor
import ru.nkyancen.playlistmaker.domain.sharing.use_case.SharingInteractorImpl

object Creator {
    private const val SEARCH_TUNES_BASE_URL = "https://itunes.apple.com/"

    const val CURRENT_TRACK_TAG = "Current Track"
    const val SEARCH_HISTORY_TAG = "Search History"
    const val IS_NIGHT_MODE_TAG = "Is_Night_Mode_On"

    private lateinit var application: Application

    fun initApplication(application: Application) {
        this.application = application
    }

    private fun getAppContext() = application.applicationContext as App

    private fun provideSharedPreferences(tag: String) =
        application.getSharedPreferences(tag, Context.MODE_PRIVATE)!!

    private fun getNightModePrefsClient(): LocalPrefsClient<Boolean> =
        NightModePrefsClient(provideSharedPreferences(IS_NIGHT_MODE_TAG), IS_NIGHT_MODE_TAG)

    private fun getNightModeRepository(): NightModeRepository =
        NightModeRepositoryImpl(getNightModePrefsClient(), getAppContext())

    fun provideNightModeInteractor() = NightModeInteractorImpl(getNightModeRepository())

    private fun getExternalNavigatorRepository(): ExternalNavigator =
        ExternalNavigatorImpl(getAppContext())

    fun provideSharingInteractor() : SharingInteractor =
        SharingInteractorImpl(getExternalNavigatorRepository())

    private fun getMediaPlayerRepository(): MediaPlayerRepository =
        MediaPlayerRepositoryImpl(MediaPlayer())

    fun providePlayerInteractor(): MediaPlayerInteractor =
        MediaPlayerInteractorImpl(getMediaPlayerRepository())

    private fun createRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(SEARCH_TUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun getRemoteClient(): RemoteClient = RetrofitClient(createRetrofit())

    private fun getTrackRepository(): TrackSearchRepository =
        TrackSearchRepositoryImpl(getRemoteClient())

    private fun getHistoryPrefsClient(): LocalPrefsClient<String> =
        HistoryPrefsClient(provideSharedPreferences(SEARCH_HISTORY_TAG), SEARCH_HISTORY_TAG)

    private val gson = Gson()

    private fun getHistoryRepository(): HistoryRepository =
        HistoryRepositoryImpl(getHistoryPrefsClient(), gson)

    fun provideTrackInteractor(): TrackInteractor =
        TrackInteractorImpl(getTrackRepository(), getHistoryRepository())
}