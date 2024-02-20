package co.statu.rule.mail

import co.statu.parsek.api.ParsekPlugin
import co.statu.parsek.api.PluginContext
import co.statu.parsek.api.config.PluginConfigManager
import co.statu.rule.database.DatabaseManager
import co.statu.rule.mail.event.DatabaseEventHandler
import co.statu.rule.mail.event.ParsekEventHandler
import co.statu.rule.mail.event.TokenEventHandler
import co.statu.rule.token.provider.TokenProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MailPlugin(pluginContext: PluginContext) : ParsekPlugin(pluginContext) {
    companion object {
        internal val logger: Logger = LoggerFactory.getLogger(MailPlugin::class.java)

        internal lateinit var pluginConfigManager: PluginConfigManager<MailConfig>

        internal lateinit var INSTANCE: MailPlugin

        internal lateinit var databaseManager: DatabaseManager

        internal lateinit var tokenProvider: TokenProvider
    }

    init {
        INSTANCE = this

        logger.info("Initialized instance")

        context.pluginEventManager.register(this, ParsekEventHandler())
        context.pluginEventManager.register(this, DatabaseEventHandler())
        context.pluginEventManager.register(this, TokenEventHandler())

        logger.info("Registered events")
    }
}
