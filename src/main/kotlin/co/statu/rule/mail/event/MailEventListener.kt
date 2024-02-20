package co.statu.rule.mail.event

import co.statu.parsek.api.PluginEvent
import co.statu.rule.mail.MailManager

interface MailEventListener : PluginEvent {
    fun onReady(mailManager: MailManager)
}