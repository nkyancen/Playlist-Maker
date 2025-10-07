package ru.nkyancen.playlistmaker.data.repository

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import ru.nkyancen.playlistmaker.data.sources.LocalPrefsClient
import ru.nkyancen.playlistmaker.domain.repository.night_mode.NightModeRepository

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
        nightModePrefsClient.save(
            isModeEnabled
        )
        setNightMode()
    }

    override fun getSettingsValue(): Boolean = nightModePrefsClient.load(
        isModeOn()
    )

    override fun setNightMode() {
        AppCompatDelegate.setDefaultNightMode(
            if (getSettingsValue()) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}