package co.statu.rule.mail.event

import co.statu.parsek.api.annotation.EventListener
import co.statu.parsek.api.config.PluginConfigManager
import co.statu.parsek.api.event.CoreEventListener
import co.statu.parsek.config.ConfigManager
import co.statu.rule.mail.MailConfig
import co.statu.rule.mail.MailPlugin
import org.slf4j.Logger

@EventListener
class CoreEventHandler(
    private val mailPlugin: MailPlugin,
    private val logger: Logger,
) : CoreEventListener {
    override suspend fun onConfigManagerReady(configManager: ConfigManager) {
        val pluginConfigManager = PluginConfigManager(
            mailPlugin,
            MailConfig::class.java,
        )

        MailPlugin.pluginConfigManager = pluginConfigManager

        logger.info("Initialized plugin config")
    }
}