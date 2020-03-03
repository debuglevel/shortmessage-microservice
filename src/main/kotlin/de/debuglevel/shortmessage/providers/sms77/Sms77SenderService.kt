package de.debuglevel.shortmessage.providers.sms77

import de.debuglevel.shortmessage.providers.MessageReceipt
import de.debuglevel.shortmessage.providers.ShortmessageSenderService
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import mu.KotlinLogging
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.inject.Singleton


@Singleton
@Requires(property = "app.shortmessage.provider", value = "sms77")
class Sms77SenderService(
    @Property(name = "app.shortmessage.providers.sms77.apikey") val apiKey: String,
    @Property(name = "app.shortmessage.providers.sms77.sender") val sender: String
) : ShortmessageSenderService {
    private val logger = KotlinLogging.logger {}

    private fun send(recipientNumber: String, body: String, sender: String, apiKey: String, type: String): String {
        val urlString = "https://gateway.sms77.io/api/sms/" +
                "?p=$apiKey" +
                "&to=$recipientNumber" +
                "&text=$body" +
                "&type=$type" +
                "&from=$sender"

        val output = getUrlContents(urlString)

        logger.debug { "Answer from API: $output" }

        return output
    }

    /**
     * remarks: example code taken from https://www.sms77.io/de/entwickler/
     */
    private fun getUrlContents(theUrl: String): String {
        val content = StringBuilder()

        // many of these calls can throw exceptions, so i've just
        // wrapped them all in one try/catch statement.
        try {
            // create a url object
            val url = URL(theUrl)

            // create a urlconnection object
            val urlConnection = url.openConnection()

            // wrap the urlconnection in a bufferedreader
            val bufferedReader = BufferedReader(
                InputStreamReader(urlConnection.getInputStream())
            )

            var line: String

            // read from the urlconnection via the bufferedreader
            content.append(bufferedReader.readText())

            bufferedReader.close()
        } catch (e: Exception) {
            logger.error(e) { "Sending failed" }
            throw e
        }

        return content.toString()
    }

    override fun send(recipientNumber: String, body: String): MessageReceipt {
        logger.debug { "Sending body '$body' to '$recipientNumber'..." }

        send(recipientNumber, body, sender, apiKey, "direct")

        logger.debug { "Sent body '$body' to '$recipientNumber'" }
        val messageReceipt = MessageReceipt()

        return messageReceipt
    }
}