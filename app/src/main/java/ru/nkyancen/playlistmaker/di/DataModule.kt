package ru.nkyancen.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.nkyancen.playlistmaker.core.utils.Constants.IS_NIGHT_MODE_TAG
import ru.nkyancen.playlistmaker.core.utils.Constants.SEARCH_HISTORY_TAG
import ru.nkyancen.playlistmaker.core.utils.Constants.SEARCH_TUNES_BASE_URL
import ru.nkyancen.playlistmaker.data.LocalPrefsClient
import ru.nkyancen.playlistmaker.data.search.sources.local.prefs.HistoryPrefsClient
import ru.nkyancen.playlistmaker.data.search.sources.remote.RemoteClient
import ru.nkyancen.playlistmaker.data.search.sources.remote.RetrofitClient
import ru.nkyancen.playlistmaker.data.search.sources.remote.TunesApi
import ru.nkyancen.playlistmaker.data.settings.sources.local.prefs.NightModePrefsClient

val dataModule = module {
    single(named("nightModePrefs")) {
        androidContext().getSharedPreferences(
            IS_NIGHT_MODE_TAG,
            Context.MODE_PRIVATE
        )
    }

    single(named("historyPrefs")) {
        androidContext().getSharedPreferences(
            SEARCH_HISTORY_TAG,
            Context.MODE_PRIVATE
        )
    }

    single<LocalPrefsClient<Boolean>>(named("nightModePrefsClient")) {
        NightModePrefsClient(get(named("nightModePrefs")), IS_NIGHT_MODE_TAG)
    }

    single<LocalPrefsClient<String>>(named("historyPrefsClient")) {
        HistoryPrefsClient(get(named("historyPrefs")),SEARCH_HISTORY_TAG)
    }

    factory {
        MediaPlayer()
    }

    singleOf(::Gson)

    single<TunesApi> {
        Retrofit.Builder()
            .baseUrl(SEARCH_TUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TunesApi::class.java)
    }

    single<RemoteClient> {
        RetrofitClient(get())
    }
}