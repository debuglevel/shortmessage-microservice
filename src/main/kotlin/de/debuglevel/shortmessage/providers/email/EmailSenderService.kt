package de.debuglevel.shortmessage.providers.email

import de.debuglevel.shortmessage.providers.MessageReceipt
import de.debuglevel.shortmessage.providers.ShortmessageSenderService
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import mu.KotlinLogging
import org.simplejavamail.api.mailer.Mailer
import org.simplejavamail.api.mailer.config.TransportStrategy
import org.simplejavamail.email.EmailBuilder
import org.simplejavamail.mailer.MailerBuilder
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Singleton

@Singleton
@Requires(property = "app.shortmessage.provider", value = "email")
class EmailSenderService(
    @Property(name = "app.shortmessage.providers.email.server.host") private val host: String,
    @Property(name = "app.shortmessage.providers.email.server.port") private val port: Int,
    @Property(name = "app.shortmessage.providers.email.server.username") private val username: String,
    @Property(name = "app.shortmessage.providers.email.server.password") private val password: String,
    @Property(name = "app.shortmessage.providers.email.recipient.name") private val name: String,
    @Property(name = "app.shortmessage.providers.email.recipient.email") private val emailAddress: String,
    @Property(name = "app.shortmessage.providers.email.sender.email") private val senderEmailAddress: String,
) : ShortmessageSenderService {
    private val logger = KotlinLogging.logger {}

    private val lastId = AtomicInteger(0)

    private val mailer: Mailer = MailerBuilder
        .withSMTPServer(host, port, username, password)
        .withTransportStrategy(TransportStrategy.SMTP_TLS) // TODO: using STARTTLS is hardcoded; extend it for configurable SMTPS and SMTP
        .buildMailer()

    override fun send(recipientNumber: String, body: String): MessageReceipt {
        logger.debug { "Sending message with body '$body' to '$recipientNumber' (if it wasn't the EmailSenderService)..." }

        sendMail(recipientNumber, body)

        val messageReceipt = MessageReceipt(
            id = lastId.incrementAndGet().toString(),
            body = body,
            price = "0.00 €",
            recipientNumber = recipientNumber,
            senderNumber = "+999 123 456789",
            status = "not sent, as email-sender used"
        )

        return messageReceipt
    }

    private fun sendMail(recipientNumber: String, body: String) {
        val email = EmailBuilder.startingBlank()
            .from("SMS Mock", senderEmailAddress)
            .to(name, emailAddress)
            .withSubject("SMS to $recipientNumber")
            .withPlainText(body)
            .buildEmail()

        mailer.sendMail(email)
    }
}