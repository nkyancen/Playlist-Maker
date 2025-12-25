package ru.nkyancen.playlistmaker.settings.domain.api

interface ExternalNavigator {
    fun shareLink()

    fun openEmail()

    fun openLink()
}