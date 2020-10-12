package de.debuglevel.shortmessage.message

import de.debuglevel.shortmessage.providers.MessageReceipt

data class AddMessageResponse(
    val id: String?,
    val body: String?,
    val senderNumber: String?,
    val recipientNumber: String?,
    val status: String?,
    val price: String?
) {
    constructor(messageReceipt: MessageReceipt) : this(
        id = messageReceipt.id,
        body = messageReceipt.body,
        senderNumber = messageReceipt.senderNumber,
        recipientNumber = messageReceipt.recipientNumber,
        status = messageReceipt.status,
        price = messageReceipt.price
    )
}