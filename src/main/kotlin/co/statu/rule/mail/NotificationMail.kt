package co.statu.rule.mail

abstract class NotificationMail : Mail {
    override val templatePath = "notification/"
}