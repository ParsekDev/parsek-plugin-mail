package co.statu.rule.mail.event

import co.statu.parsek.PluginEventManager
import co.statu.parsek.api.annotation.EventListener
import co.statu.rule.database.DatabaseManager
import co.statu.rule.mail.MailClientProvider
import co.statu.rule.mail.MailManager
import co.statu.rule.mail.MailPlugin
import co.statu.rule.mail.MailPlugin.Companion.pluginConfigManager
import co.statu.rule.token.event.TokenEventListener
import co.statu.rule.token.provider.TokenProvider
import io.vertx.core.Vertx
import io.vertx.ext.web.templ.handlebars.HandlebarsTemplateEngine

@EventListener
class TokenEventHandler(
    private val mailPlugin: MailPlugin,
    private val vertx: Vertx
) : TokenEventListener {
    private val databaseManager by lazy {
        mailPlugin.pluginBeanContext.getBean(DatabaseManager::class.java)
    }

    override suspend fun onReady(tokenProvider: TokenProvider) {
        val mailClientProvider =
            MailClientProvider.create(vertx, pluginConfigManager)

        val mailManager = MailManager(
            pluginConfigManager,
            HandlebarsTemplateEngine.create(vertx),
            mailClientProvider,
            databaseManager,
            tokenProvider,
            mailPlugin
        )

        mailPlugin.registerSingletonGlobal(mailManager)

        val handlers = PluginEventManager.getEventListeners<MailEventListener>()

        handlers.forEach {
            it.onReady(mailManager)
        }
    }
}