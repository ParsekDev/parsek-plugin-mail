package co.statu.rule.mail.event

import co.statu.rule.mail.MailPlugin
import co.statu.rule.token.event.TokenEventListener
import co.statu.rule.token.provider.TokenProvider

class TokenEventHandler : TokenEventListener {
    override suspend fun onReady(tokenProvider: TokenProvider) {
        MailPlugin.tokenProvider = tokenProvider
    }
}