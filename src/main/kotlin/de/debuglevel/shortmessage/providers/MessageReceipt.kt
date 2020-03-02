package de.debuglevel.shortmessage.providers

data class MessageReceipt(
    val id: String?,
    val body: String?,
    val senderNumber: String?,
    val recipientNumber: String?,
    val status: String?,
    val price: String?
)