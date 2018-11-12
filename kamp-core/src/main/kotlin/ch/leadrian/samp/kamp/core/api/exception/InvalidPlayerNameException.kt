package ch.leadrian.samp.kamp.core.api.exception

class InvalidPlayerNameException(val name: String, message: String) : RuntimeException("Invalid player name '$name': $message")
