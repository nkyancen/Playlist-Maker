package ru.nkyancen.playlistmaker

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import ru.nkyancen.playlistmaker.data.repository.HistoryRepositoryImpl
import ru.nkyancen.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import ru.nkyancen.playlistmaker.data.repository.NightModeRepositoryImpl
import ru.nkyancen.playlistmaker.data.repository.TrackRepositoryImpl
import ru.nkyancen.playlistmaker.data.sources.LocalPrefsClient
import ru.nkyancen.playlistmaker.data.sources.RemoteClient
import ru.nkyancen.playlistmaker.data.sources.local.prefs.HistoryPrefsClient
import ru.nkyancen.playlistmaker.data.sources.local.prefs.NightModePrefsClient
import ru.nkyancen.playlistmaker.data.sources.remote.RetrofitClient
import ru.nkyancen.playlistmaker.domain.repository.media_player.MediaPlayerInteractor
import ru.nkyancen.playlistmaker.domain.repository.media_player.MediaPlayerRepository
import ru.nkyancen.playlistmaker.domain.repository.night_mode.NightModeRepository
import ru.nkyancen.playlistmaker.domain.repository.track.HistoryRepository
import ru.nkyancen.playlistmaker.domain.repository.track.TrackInteractor
import ru.nkyancen.playlistmaker.domain.repository.track.TrackRepository
import ru.nkyancen.playlistmaker.domain.use_case.MediaPlayerInteractorImpl
import ru.nkyancen.playlistmaker.domain.use_case.NightModeInteractorImpl
import ru.nkyancen.playlistmaker.domain.use_case.TrackInteractorImpl

object Creator {
    private lateinit var application: Application

    fun initApplication(application: Application) {
        this.application = application
    }

    private fun getAppContext() = application.applicationContext as App

    private fun provideSharedPreferences(tag: String) =
        application.getSharedPreferences(tag, Context.MODE_PRIVATE)!!


    private lateinit var nightModeTag: String

    fun setNightModeTag(tag: String) {
        this.nightModeTag = tag
    }

    private fun getNightModePrefsClient(tag: String): LocalPrefsClient<Boolean> =
        NightModePrefsClient(provideSharedPreferences(tag), tag)

    private fun getNightModeRepository(tag: String): NightModeRepository =
        NightModeRepositoryImpl(getNightModePrefsClient(tag), getAppContext())

    fun provideNightModeInteractor() = NightModeInteractorImpl(getNightModeRepository(nightModeTag))

    private fun getMediaPlayerRepository(): MediaPlayerRepository =
        MediaPlayerRepositoryImpl(MediaPlayer())

    fun providePlayerInteractor(): MediaPlayerInteractor =
        MediaPlayerInteractorImpl(getMediaPlayerRepository())


    private fun getRemoteClient(): RemoteClient = RetrofitClient

    private fun getTrackRepository(): TrackRepository =
        TrackRepositoryImpl(getRemoteClient())

    private fun getHistoryPrefsClient(tag: String): LocalPrefsClient<String> =
        HistoryPrefsClient(provideSharedPreferences(tag), tag)

    private val gson = Gson()

    private fun getHistoryRepository(tag: String): HistoryRepository =
        HistoryRepositoryImpl(getHistoryPrefsClient(tag), gson)

    fun provideTrackInteractor(tag: String): TrackInteractor =
        TrackInteractorImpl(getTrackRepository(), getHistoryRepository(tag))
}