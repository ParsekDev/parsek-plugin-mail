package co.statu.rule.mail

import co.statu.parsek.api.config.PluginConfig

data class MailConfig(
    val uiAddress: String = "",
    val brandName: String = "Parsek",
    val address: String = "",
    val host: String = "",
    val port: Int = 465,
    val username: String = "",
    val password: String = "",
    val ssl: Boolean = true,
    val tls: Boolean = true,
    val authMethod: String? = "",
    val templateFilePrefix: String = "../mail/"
) : PluginConfig()