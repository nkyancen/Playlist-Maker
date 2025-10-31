package ru.nkyancen.playlistmaker.data.settings.sources.local.prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import ru.nkyancen.playlistmaker.data.LocalPrefsClient

class NightModePrefsClient(
    private val sharedPrefs: SharedPreferences,
    private val tag: String
) : LocalPrefsClient<Boolean> {

    override fun saveData(data: Boolean) {
        sharedPrefs.edit {
            putBoolean(
                tag,
                data
            )
        }
    }

    override fun loadData(default: Boolean) = sharedPrefs.getBoolean(
        tag,
        default
    )
}