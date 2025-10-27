package ru.nkyancen.playlistmaker.core

import android.app.Application
import android.content.res.Configuration
import ru.nkyancen.playlistmaker.core.creator.Creator

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        val themeManager = Creator.provideNightModeInteractor()

        themeManager.setNightMode()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val themeManager = Creator.provideNightModeInteractor()

        themeManager.setNightMode()
    }

}