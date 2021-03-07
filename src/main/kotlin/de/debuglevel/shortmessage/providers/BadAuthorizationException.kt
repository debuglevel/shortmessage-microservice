package de.debuglevel.shortmessage.providers

data class BadAuthorizationException(val msg: String, val inner: Throwable) : Exception(msg, inner)