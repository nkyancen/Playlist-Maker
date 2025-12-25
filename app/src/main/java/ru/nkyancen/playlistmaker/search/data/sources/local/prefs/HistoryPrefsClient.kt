package ru.nkyancen.playlistmaker.search.data.sources.local.prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import ru.nkyancen.playlistmaker.core.utils.LocalPrefsClient

class HistoryPrefsClient(
    private val sharedPrefs: SharedPreferences,
    private val tag: String
) : LocalPrefsClient<String> {

    override fun saveData(data: String) {
        sharedPrefs.edit {
            putString(
                tag,
                data
            )
        }
    }

    override fun loadData(default: String): String = sharedPrefs.getString(
        tag,
        default
    )!!
}