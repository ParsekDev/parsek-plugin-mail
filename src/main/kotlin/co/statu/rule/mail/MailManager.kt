package co.statu.rule.mail

import co.statu.parsek.api.config.PluginConfigManager
import co.statu.rule.database.DatabaseManager
import co.statu.rule.token.provider.TokenProvider
import io.vertx.ext.mail.MailMessage
import io.vertx.ext.web.templ.handlebars.HandlebarsTemplateEngine
import io.vertx.kotlin.coroutines.await
import java.util.*

class MailManager(
    private val pluginConfigManager: PluginConfigManager<MailConfig>,
    private val templateEngine: HandlebarsTemplateEngine,
    private val mailClientProvider: MailClientProvider,
    private val databaseManager: DatabaseManager,
    private val tokenProvider: TokenProvider
) {
    private val mailClient by lazy {
        mailClientProvider.provide()
    }

    suspend fun sendMail(userId: UUID = UUID.randomUUID(), emailAddress: String, mail: Mail) {
        val jdbcPool = databaseManager.getConnectionPool()

        val emailConfig = pluginConfigManager.config
        val message = MailMessage()

        message.from = emailConfig.address + " (${emailConfig.brandName})"
        message.subject = mail.subject
        message.setTo(emailAddress)

        val parameters = mail.parameterGenerator(
            emailAddress,
            userId,
            emailConfig.uiAddress,
            databaseManager,
            jdbcPool,
            tokenProvider
        )

        val mailPlugin = MailPlugin.INSTANCE
        val context = mailPlugin.context
        val environmentType = context.environmentType
        val stage = context.releaseStage

        parameters.put("ENVIRONMENT", environmentType)
        parameters.put("STAGE", stage)
        parameters.put("uiAddress", emailConfig.uiAddress)

        message.html = templateEngine.render(
            parameters, emailConfig.templateFilePrefix + mail.templatePath
        ).await().toString()

        mailClient.sendMail(message).await()
    }
}