package ru.nkyancen.playlistmaker.settings.domain.api

interface ExternalNavigator {
    fun shareText(sharedText: String)

    fun openEmail()

    fun openLink()
}