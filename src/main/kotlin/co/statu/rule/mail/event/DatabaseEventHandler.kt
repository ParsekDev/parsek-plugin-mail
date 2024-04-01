package co.statu.rule.mail.event

import co.statu.parsek.api.annotation.EventListener
import co.statu.rule.database.DatabaseManager
import co.statu.rule.database.event.DatabaseEventListener
import co.statu.rule.mail.MailPlugin

@EventListener
class DatabaseEventHandler(private val mailPlugin: MailPlugin) : DatabaseEventListener {

    override suspend fun onReady(databaseManager: DatabaseManager) {
        databaseManager.migrateNewPluginId("mail", mailPlugin.pluginId, mailPlugin)
    }
}