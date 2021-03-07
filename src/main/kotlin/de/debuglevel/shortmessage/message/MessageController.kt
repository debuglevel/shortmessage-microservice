package de.debuglevel.shortmessage.message

import de.debuglevel.shortmessage.providers.BadAuthorizationException
import de.debuglevel.shortmessage.providers.ShortmessageSenderService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import mu.KotlinLogging

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/messages")
class MessageController(private val shortmessageSenderService: ShortmessageSenderService) {
    private val logger = KotlinLogging.logger {}

    @Post("/")
    fun postOne(addMessageRequest: AddMessageRequest): HttpResponse<*> {
        logger.debug("Called postOne($addMessageRequest)")

        return try {
            val messageReceipt =
                shortmessageSenderService.send(addMessageRequest.recipientNumber, addMessageRequest.body)

            HttpResponse.created(AddMessageResponse(messageReceipt))
        } catch (e: BadAuthorizationException) {
            logger.error(e) { "Could not send message due to bad authorization" }
            HttpResponse.serverError("Could not send message due to bad authorization")
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError("Unhandled exception")
        }
    }
}