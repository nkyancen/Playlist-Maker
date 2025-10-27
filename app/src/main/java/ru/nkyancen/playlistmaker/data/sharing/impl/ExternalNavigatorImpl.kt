package ru.nkyancen.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.domain.sharing.api.ExternalNavigator

class ExternalNavigatorImpl(
    private val appContext: Context
) : ExternalNavigator {
    override fun shareLink() {
        val shareUrl = appContext.getString(R.string.share_text_url)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "plain/text"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            shareUrl
        )

        appContext.startActivity(
            Intent.createChooser(shareIntent, null)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override fun openEmail() {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        val targetEmails = appContext.resources.getStringArray(R.array.email_list).joinToString()
        val title = appContext.getString(R.string.mail_title)
        val content = appContext.getString(R.string.mail_text)

        supportIntent.data = "mailto: $targetEmails".toUri()
        supportIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            title
        )
        supportIntent.putExtra(
            Intent.EXTRA_TEXT,
            content
        )
        appContext.startActivity(supportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override fun openLink() {
        val userAgreementUrl = appContext.getString(R.string.offer_url)
        val userAgreementIntent = Intent(Intent.ACTION_VIEW)
        userAgreementIntent.data = userAgreementUrl.toUri()
        appContext.startActivity(userAgreementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}