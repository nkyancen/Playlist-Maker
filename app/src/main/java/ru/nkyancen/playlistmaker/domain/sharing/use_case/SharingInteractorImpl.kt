package ru.nkyancen.playlistmaker.domain.sharing.use_case

import ru.nkyancen.playlistmaker.domain.sharing.api.ExternalNavigator
import ru.nkyancen.playlistmaker.domain.sharing.api.SharingInteractor

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