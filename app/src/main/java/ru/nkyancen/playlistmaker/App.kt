package ru.nkyancen.playlistmaker

import android.app.Application
import android.content.res.Configuration


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        Creator.setNightModeTag(IS_NIGHT_MODE_TAG)
        val themeManager = Creator.provideNightModeInteractor()

        themeManager.setNightMode()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val themeManager = Creator.provideNightModeInteractor()

        themeManager.setNightMode()
    }

    companion object {
        private const val IS_NIGHT_MODE_TAG = "Is_Night_Mode_On"
    }
}