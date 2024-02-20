package co.statu.rule.mail

import co.statu.parsek.api.config.PluginConfigManager
import io.vertx.core.Vertx
import io.vertx.ext.mail.MailClient
import io.vertx.ext.mail.MailConfig
import io.vertx.ext.mail.StartTLSOptions

class MailClientProvider private constructor(
    vertx: Vertx,
    pluginConfigManager: PluginConfigManager<co.statu.rule.mail.MailConfig>
) {
    companion object {
        fun create(
            vertx: Vertx,
            pluginConfigManager: PluginConfigManager<co.statu.rule.mail.MailConfig>
        ) = MailClientProvider(vertx, pluginConfigManager)
    }

    private val mailClient by lazy {
        val mailClientConfig = MailConfig()
        val emailConfig = pluginConfigManager.config

        mailClientConfig.hostname = emailConfig.host
        mailClientConfig.port = emailConfig.port

        if (emailConfig.ssl) {
            mailClientConfig.isSsl = true
        }

        if (emailConfig.tls) {
            mailClientConfig.starttls = StartTLSOptions.REQUIRED
        }

        mailClientConfig.username = emailConfig.username
        mailClientConfig.password = emailConfig.password

        if (!emailConfig.authMethod.isNullOrEmpty()) {
            mailClientConfig.authMethods = emailConfig.authMethod
        }

        MailClient.createShared(vertx, mailClientConfig, "mailClient")
    }

    fun provide(): MailClient = mailClient
}