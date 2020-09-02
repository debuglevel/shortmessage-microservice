package de.debuglevel.shortmessage.message

data class AddMessageRequest(
    val recipientNumber: String,
    val body: String
)