package de.debuglevel.shortmessage.message

data class MessageRequest(
    val recipientNumber: String,
    val body: String
)