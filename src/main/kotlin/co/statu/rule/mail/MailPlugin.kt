package co.statu.rule.mail

import co.statu.parsek.PluginEventManager
import co.statu.parsek.api.ParsekPlugin
import co.statu.parsek.api.config.PluginConfigManager
import co.statu.rule.database.DatabaseManager
import co.statu.rule.mail.event.MailEventListener
import co.statu.rule.token.provider.TokenProvider
import io.vertx.ext.web.templ.handlebars.HandlebarsTemplateEngine
import org.springframework.beans.factory.getBean

class MailPlugin : ParsekPlugin() {
    companion object {
        internal lateinit var pluginConfigManager: PluginConfigManager<MailConfig>
    }

    override suspend fun onStart() {
        val pluginConfigManager = PluginConfigManager(
            this,
            MailConfig::class.java,
        )

        MailPlugin.pluginConfigManager = pluginConfigManager

        logger.info("Initialized plugin config")

        val databaseManager = pluginGlobalBeanContext.beanFactory.getBean<DatabaseManager>()
        val tokenProvider = pluginGlobalBeanContext.beanFactory.getBean<TokenProvider>()

        val mailClientProvider =
            MailClientProvider.create(vertx, pluginConfigManager)

        val mailManager = MailManager(
            pluginConfigManager,
            HandlebarsTemplateEngine.create(vertx),
            mailClientProvider,
            databaseManager,
            tokenProvider,
            this
        )

        registerSingletonGlobal(mailManager)

        val handlers = PluginEventManager.getEventListeners<MailEventListener>()

        handlers.forEach {
            it.onReady(mailManager)
        }
    }
}
