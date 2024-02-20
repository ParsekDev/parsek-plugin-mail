package co.statu.rule.mail.event

import co.statu.rule.database.DatabaseManager
import co.statu.rule.database.event.DatabaseEventListener
import co.statu.rule.mail.MailPlugin

class DatabaseEventHandler : DatabaseEventListener {

    override suspend fun onReady(databaseManager: DatabaseManager) {
        MailPlugin.databaseManager = databaseManager
    }
}