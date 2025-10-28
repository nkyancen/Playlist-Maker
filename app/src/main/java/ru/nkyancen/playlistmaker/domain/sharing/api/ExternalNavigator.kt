package ru.nkyancen.playlistmaker.domain.sharing.api

interface ExternalNavigator {
    fun shareLink()

    fun openEmail()

    fun openLink()
}