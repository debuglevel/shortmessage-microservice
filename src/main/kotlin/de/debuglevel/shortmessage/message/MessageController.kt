package de.debuglevel.shortmessage.message

import de.debuglevel.shortmessage.providers.ShortmessageSenderService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import mu.KotlinLogging

@Controller("/messages")
class MessageController(private val shortmessageSenderService: ShortmessageSenderService) {
    private val logger = KotlinLogging.logger {}

    @Post("/")
    fun postOne(messageRequest: MessageRequest): HttpResponse<MessageResponse> {
        logger.debug("Called postOne($messageRequest)")

        return try {
            val messageReceipt = shortmessageSenderService.send(messageRequest.recipientNumber, messageRequest.body)

            HttpResponse.created(MessageResponse(messageReceipt))
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError<MessageResponse>()
        }
    }
}