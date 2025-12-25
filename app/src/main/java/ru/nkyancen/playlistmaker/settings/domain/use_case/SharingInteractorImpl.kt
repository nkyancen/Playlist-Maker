package ru.nkyancen.playlistmaker.settings.domain.use_case

import ru.nkyancen.playlistmaker.settings.domain.api.ExternalNavigator
import ru.nkyancen.playlistmaker.settings.domain.api.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink()
    }

    override fun openSupport() {
        externalNavigator.openEmail()
    }

    override fun openTerms() {
        externalNavigator.openLink()
    }

}