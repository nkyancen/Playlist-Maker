package ru.nkyancen.playlistmaker.settings.domain.api

interface SharingInteractor {
    fun shareApp(sharedUrl: String)

    fun openSupport()

    fun openTerms()
}