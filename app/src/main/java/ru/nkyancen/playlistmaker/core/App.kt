package ru.nkyancen.playlistmaker.core

import android.app.Application
import android.content.res.Configuration
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.nkyancen.playlistmaker.di.dataModule
import ru.nkyancen.playlistmaker.di.interactorModule
import ru.nkyancen.playlistmaker.di.repositoryModule
import ru.nkyancen.playlistmaker.di.viewModelModule
import ru.nkyancen.playlistmaker.domain.settings.api.NightModeInteractor

class App : Application() {
    private val themeManager : NightModeInteractor by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(viewModelModule, repositoryModule, interactorModule, dataModule)
        }

        themeManager.setNightMode()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        themeManager.setNightMode()
    }
}