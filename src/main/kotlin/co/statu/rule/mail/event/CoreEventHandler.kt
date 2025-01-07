package co.statu.rule.mail.event

import co.statu.parsek.PluginEventManager
import co.statu.parsek.api.annotation.EventListener
import co.statu.parsek.api.config.PluginConfigManager
import co.statu.parsek.api.event.CoreEventListener
import co.statu.parsek.config.ConfigManager
import co.statu.rule.database.DatabaseManager
import co.statu.rule.mail.MailClientProvider
import co.statu.rule.mail.MailConfig
import co.statu.rule.mail.MailManager
import co.statu.rule.mail.MailPlugin
import co.statu.rule.token.provider.TokenProvider
import io.vertx.core.Vertx
import io.vertx.ext.web.templ.handlebars.HandlebarsTemplateEngine
import org.slf4j.Logger

@EventListener
class CoreEventHandler(
    private val mailPlugin: MailPlugin,
    private val logger: Logger,
    private val vertx: Vertx
) : CoreEventListener {
    private val databaseManager by lazy {
        mailPlugin.pluginBeanContext.getBean(DatabaseManager::class.java)
    }

    private val tokenProvider by lazy {
        mailPlugin.pluginBeanContext.getBean(TokenProvider::class.java)
    }

    override suspend fun onConfigManagerReady(configManager: ConfigManager) {
        val pluginConfigManager = PluginConfigManager(
            mailPlugin,
            MailConfig::class.java,
        )

        MailPlugin.pluginConfigManager = pluginConfigManager

        logger.info("Initialized plugin config")

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