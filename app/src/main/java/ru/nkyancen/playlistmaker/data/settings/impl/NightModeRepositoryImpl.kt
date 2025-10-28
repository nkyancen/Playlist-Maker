package ru.nkyancen.playlistmaker.data.settings.impl

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import ru.nkyancen.playlistmaker.data.search.sources.local.prefs.LocalPrefsClient
import ru.nkyancen.playlistmaker.domain.settings.api.NightModeRepository

class NightModeRepositoryImpl(
    private val nightModePrefsClient: LocalPrefsClient<Boolean>,
    private val appContext: Context
) : NightModeRepository {

    private fun isModeOn(): Boolean {
        val darkModeFlag =
            appContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }

    override fun switchMode(isModeEnabled: Boolean) {
        nightModePrefsClient.saveData(
            isModeEnabled
        )
        setNightMode()
    }

    override fun isNightModeFromSettings(): Boolean = nightModePrefsClient.loadData(
        isModeOn()
    )

    override fun setNightMode() {
        AppCompatDelegate.setDefaultNightMode(
            if (isNightModeFromSettings()) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}