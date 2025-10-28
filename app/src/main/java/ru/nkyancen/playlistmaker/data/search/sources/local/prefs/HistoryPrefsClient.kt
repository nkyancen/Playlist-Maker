package ru.nkyancen.playlistmaker.data.search.sources.local.prefs

import android.content.SharedPreferences
import androidx.core.content.edit

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