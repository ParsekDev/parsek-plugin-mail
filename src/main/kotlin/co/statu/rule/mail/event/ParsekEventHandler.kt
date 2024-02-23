package co.statu.rule.mail.event

import co.statu.parsek.api.config.PluginConfigManager
import co.statu.parsek.api.event.ParsekEventListener
import co.statu.parsek.config.ConfigManager
import co.statu.rule.mail.MailClientProvider
import co.statu.rule.mail.MailConfig
import co.statu.rule.mail.MailManager
import co.statu.rule.mail.MailPlugin
import co.statu.rule.mail.MailPlugin.Companion.logger
import co.statu.rule.mail.config.migration.ConfigMigration1to2
import io.vertx.ext.web.templ.handlebars.HandlebarsTemplateEngine

class ParsekEventHandler : ParsekEventListener {
    override suspend fun onConfigManagerReady(configManager: ConfigManager) {
        MailPlugin.pluginConfigManager = PluginConfigManager(
            configManager,
            MailPlugin.INSTANCE,
            MailConfig::class.java,
            logger,
            listOf(ConfigMigration1to2()),
            listOf("mail")
        )

        logger.info("Initialized plugin config")

        val vertx = MailPlugin.INSTANCE.context.vertx
        val mailClientProvider =
            MailClientProvider.create(MailPlugin.INSTANCE.context.vertx, MailPlugin.pluginConfigManager)

        val mailManager = MailManager(
            MailPlugin.pluginConfigManager,
            HandlebarsTemplateEngine.create(vertx),
            mailClientProvider,
            MailPlugin.databaseManager,
            MailPlugin.tokenProvider
        )

        val handlers = MailPlugin.INSTANCE.context.pluginEventManager.getEventHandlers<MailEventListener>()

        handlers.forEach {
            it.onReady(mailManager)
        }
    }
}