package ru.nkyancen.playlistmaker.data.sources.local.prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import ru.nkyancen.playlistmaker.data.sources.LocalPrefsClient

class NightModePrefsClient(
    private val sharedPrefs: SharedPreferences,
    private val tag: String
) : LocalPrefsClient<Boolean> {

    override fun save(data: Boolean) {
        sharedPrefs.edit {
            putBoolean(
                tag,
                data
            )
        }
    }

    override fun load(default: Boolean) = sharedPrefs.getBoolean(
        tag,
        default
    )
}