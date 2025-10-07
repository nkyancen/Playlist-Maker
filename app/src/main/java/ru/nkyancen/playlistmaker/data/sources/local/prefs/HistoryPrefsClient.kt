package ru.nkyancen.playlistmaker.data.sources.local.prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import ru.nkyancen.playlistmaker.data.sources.LocalPrefsClient

class HistoryPrefsClient(
    private val sharedPrefs: SharedPreferences,
    private val tag: String
) : LocalPrefsClient<String> {

    override fun save(data: String) {
        sharedPrefs.edit {
            putString(
                tag,
                data
            )
        }
    }

    override fun load(default: String): String = sharedPrefs.getString(
        tag,
        default
    )!!

}