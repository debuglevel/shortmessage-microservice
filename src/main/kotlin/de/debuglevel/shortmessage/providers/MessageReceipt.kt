package de.debuglevel.shortmessage.providers

data class MessageReceipt(
    val id: String? = null,
    val body: String? = null,
    val senderNumber: String? = null,
    val recipientNumber: String? = null,
    val status: String? = null,
    val price: String? = null
)