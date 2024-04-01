package co.statu.rule.mail.event

import co.statu.parsek.api.event.PluginEventListener
import co.statu.rule.mail.MailManager

interface MailEventListener : PluginEventListener {
    fun onReady(mailManager: MailManager)
}