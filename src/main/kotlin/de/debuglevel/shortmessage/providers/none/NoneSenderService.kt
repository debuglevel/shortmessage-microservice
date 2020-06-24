package de.debuglevel.shortmessage.providers.none

import de.debuglevel.shortmessage.providers.MessageReceipt
import de.debuglevel.shortmessage.providers.ShortmessageSenderService
import io.micronaut.context.annotation.Requires
import mu.KotlinLogging
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Singleton

@Singleton
@Requires(property = "app.shortmessage.provider", value = "none")
class NoneSenderService : ShortmessageSenderService {
    private val logger = KotlinLogging.logger {}

    private val lastId = AtomicInteger(0)

    override fun send(recipientNumber: String, body: String): MessageReceipt {
        logger.debug { "Sending body '$body' to '$recipientNumber' (if it wasn't the NoneSenderService)..." }

        val messageReceipt = MessageReceipt(
            id = lastId.incrementAndGet().toString(),
            body = body,
            price = "0.00 €",
            recipientNumber = recipientNumber,
            senderNumber = "+999 123 456789",
            status = "not sent, as none-sender used"
        )

        return messageReceipt
    }
}