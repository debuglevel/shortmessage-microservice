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

    override fun send(recipientNumber: String, body: String): MessageReceipt {
        logger.debug { "Sending message with body '$body' to '$recipientNumber'..." }

        send(recipientNumber, body, sender, apiKey, "direct")

        logger.debug { "Sent message with body '$body' to '$recipientNumber'" }
        val messageReceipt = MessageReceipt()

        return messageReceipt
    }

    private fun send(recipientNumber: String, body: String, sender: String, apiKey: String, type: String): String {
        val url = "https://gateway.sms77.io/api/sms/" +
                "?p=$apiKey" +
                "&to=$recipientNumber" +
                "&text=$body" +
                "&type=$type" +
                "&from=$sender"

        logger.debug { "Sending request to URL $url" }
        val response = getUrlContents(URL(url))
        logger.debug { "Sent request, answer from API: $response" }

        return response
    }

    /**
     * Remarks: example code taken from https://www.sms77.io/de/entwickler/ and reduced to Kotlin style
     */
    private fun getUrlContents(url: URL): String {
        return try {
            val urlConnection = url.openConnection()
            BufferedReader(InputStreamReader(urlConnection.getInputStream())).use {
                it.readText()
            }
        } catch (e: Exception) {
            logger.error(e) { "Sending failed" }
            throw e
        }
    }
}