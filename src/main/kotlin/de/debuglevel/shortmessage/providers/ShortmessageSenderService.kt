package de.debuglevel.shortmessage.providers

interface ShortmessageSenderService {
    fun send(recipientNumber: String, body: String): MessageReceipt
}