package de.debuglevel.shortmessage.providers.esendex

import de.debuglevel.shortmessage.providers.BadAuthorizationException
import de.debuglevel.shortmessage.providers.MessageReceipt
import de.debuglevel.shortmessage.providers.ShortmessageSenderService
import esendex.sdk.java.EsendexException
import esendex.sdk.java.ServiceFactory
import esendex.sdk.java.http.HttpException
import esendex.sdk.java.model.domain.request.SmsMessageRequest
import esendex.sdk.java.service.MessagingService
import esendex.sdk.java.service.auth.UserPassword
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import mu.KotlinLogging
import javax.inject.Singleton

@Singleton
@Requires(property = "app.shortmessage.provider", value = "esendex")
class EsendexSenderService(
    @Property(name = "app.shortmessage.providers.esendex.username") val username: String,
    @Property(name = "app.shortmessage.providers.esendex.password") val password: String,
    @Property(name = "app.shortmessage.providers.esendex.accountreference") val accountReference: String
) : ShortmessageSenderService {
    private val logger = KotlinLogging.logger {}

    private val messagingService: MessagingService

    init {
        logger.debug { "Initializing Esendex with username '$username'..." }

        val userPassword = UserPassword(username, password)
        val serviceFactory = ServiceFactory.createBasicAuthenticatingFactory(userPassword)
        this.messagingService = serviceFactory.messagingService
    }

    override fun send(recipientNumber: String, body: String): MessageReceipt {
        logger.debug { "Sending message with body '$body' to '$recipientNumber'..." }

        val messageRequest = SmsMessageRequest(recipientNumber, body)
        return try {
            val response = messagingService.sendMessage(accountReference, messageRequest)
            logger.debug { "Sent message with body '$body' to '$recipientNumber': $response" }
            MessageReceipt(id = response.batchId)
        } catch (e: HttpException) {
            logger.error(e) { "Sending message failed" }

            if (e.message!!.contains("403")) {
                throw BadAuthorizationException("Esendex library failed with a HTTP status 403: ${e.message}", e)
            } else {
                throw e
            }
        } catch (e: EsendexException) {
            logger.error(e) { "Sending message failed" }
            throw e
        }
    }
}