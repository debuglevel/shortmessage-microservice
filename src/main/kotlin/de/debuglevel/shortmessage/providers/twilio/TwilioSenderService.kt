package de.debuglevel.shortmessage.providers.twilio

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import de.debuglevel.shortmessage.providers.MessageReceipt
import de.debuglevel.shortmessage.providers.ShortmessageSenderService
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import mu.KotlinLogging
import javax.inject.Singleton

@Singleton
@Requires(property = "app.shortmessage.provider", value = "twilio")
class TwilioSenderService(
    @Property(name = "app.shortmessage.providers.twilio.accountsid") val accountSid: String,
    @Property(name = "app.shortmessage.providers.twilio.authtoken") val authToken: String,
    @Property(name = "app.shortmessage.providers.twilio.sendernumber") val senderNumber: String
) : ShortmessageSenderService {
    private val logger = KotlinLogging.logger {}

    init {
        logger.debug { "Initializing Twilio with account '$accountSid'..." }
        Twilio.init(accountSid, authToken)
    }

    override fun send(recipientNumber: String, body: String): MessageReceipt {
        logger.debug { "Sending message with body '$body' to '$recipientNumber'..." }

        val message = Message
            .creator(
                PhoneNumber(recipientNumber),
                PhoneNumber(senderNumber),
                body
            )
            .create()

        logger.debug { "Sent message with body '$body' to '$recipientNumber': $message" }
        val messageReceipt = MessageReceipt(
            id = message.sid,
            body = message.body,
            price = "${message.price} ${message.priceUnit}",
            recipientNumber = message.to,
            senderNumber = message.from.toString(),
            status = message.status.toString()
        )

        return messageReceipt
    }
}