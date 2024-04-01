package co.statu.rule.mail

import co.statu.parsek.api.ParsekPlugin
import co.statu.parsek.api.config.PluginConfigManager

class MailPlugin : ParsekPlugin() {
    companion object {
        internal lateinit var pluginConfigManager: PluginConfigManager<MailConfig>
    }
}
