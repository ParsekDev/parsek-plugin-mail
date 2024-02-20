package co.statu.rule.mail

import co.statu.rule.database.DatabaseManager
import co.statu.rule.token.provider.TokenProvider
import io.vertx.core.json.JsonObject
import io.vertx.jdbcclient.JDBCPool
import java.util.*

interface Mail {
    val templatePath: String

    val subject: String

    fun getBrandName() = MailPlugin.pluginConfigManager.config.brandName

    suspend fun parameterGenerator(
        email: String,
        userId: UUID,
        uiAddress: String,
        databaseManager: DatabaseManager,
        jdbcPool: JDBCPool,
        tokenProvider: TokenProvider
    ): JsonObject
}