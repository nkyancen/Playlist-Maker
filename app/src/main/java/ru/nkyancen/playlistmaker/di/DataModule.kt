package ru.nkyancen.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.nkyancen.playlistmaker.core.utils.LocalPrefsClient
import ru.nkyancen.playlistmaker.medialibrary.data.db.AppDatabase
import ru.nkyancen.playlistmaker.search.data.sources.local.prefs.HistoryPrefsClient
import ru.nkyancen.playlistmaker.search.data.sources.remote.RemoteClient
import ru.nkyancen.playlistmaker.search.data.sources.remote.RetrofitClient
import ru.nkyancen.playlistmaker.search.data.sources.remote.TunesApi
import ru.nkyancen.playlistmaker.settings.data.source.local.prefs.NightModePrefsClient

private const val NIGHT_MODE_PREFS = "nightModePrefs"
const val NIGHT_MODE_PREFS_CLIENT = "nightModePrefsClient"

private const val HISTORY_PREFS = "historyPrefs"
const val HISTORY_PREFS_CLIENT = "historyPrefsClient"

private const val SEARCH_HISTORY_TAG = "Search History"
private const val IS_NIGHT_MODE_TAG = "Is_Night_Mode_On"

private const val SEARCH_TUNES_BASE_URL = "https://itunes.apple.com/"

val dataModule = module {
    single(named(NIGHT_MODE_PREFS)) {
        androidContext().getSharedPreferences(
            IS_NIGHT_MODE_TAG,
            Context.MODE_PRIVATE
        )
    }

    single(named(HISTORY_PREFS)) {
        androidContext().getSharedPreferences(
            SEARCH_HISTORY_TAG,
            Context.MODE_PRIVATE
        )
    }

    single<LocalPrefsClient<Boolean>>(named(NIGHT_MODE_PREFS_CLIENT)) {
        NightModePrefsClient(get(named(NIGHT_MODE_PREFS)), IS_NIGHT_MODE_TAG)
    }

    single<LocalPrefsClient<String>>(named(HISTORY_PREFS_CLIENT)) {
        HistoryPrefsClient(get(named(HISTORY_PREFS)),SEARCH_HISTORY_TAG)
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

    single {
        Room.databaseBuilder(
            androidContext(), AppDatabase::class.java, "database.db"
        ).build()
    }

    single {
        get<AppDatabase>().favoritesDao()
    }

    single {
        get<AppDatabase>().playlistDao()
    }

}