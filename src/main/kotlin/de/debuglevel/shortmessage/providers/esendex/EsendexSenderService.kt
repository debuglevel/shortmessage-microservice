package de.debuglevel.shortmessage.providers.esendex

//@Singleton
//class EsendexSenderService : ShortmessageSenderService {
//    private val logger = KotlinLogging.logger {}
//
//    override fun send(recipientNumber: String, body: String): MessageReceipt
//    {
//        logger.debug { "Sending body '$body' to '$recipientNumber'..." }
//
//        val userPassword = UserPassword("YourUsername", "YourPassword")
//        val serviceFactory = ServiceFactory.createBasicAuthenticatingFactory(userPassword)
//        val messagingService = serviceFactory.messagingService
//
//        val messageRequest = SmsMessageRequest(recipientNumber, body)
//        val response: MessageResultResponse
//        return try {
//            response = messagingService.sendMessage("YourAccountReference", messageRequest)
//            logger.debug { "Sent body '$body' to '$recipientNumber': $response" }
//            MessageReceipt(response.batchId)
//        } catch (e: EsendexException) {
//            logger.error(e) { "Sending body failed" }
//            throw e
//        }
//    }
//}