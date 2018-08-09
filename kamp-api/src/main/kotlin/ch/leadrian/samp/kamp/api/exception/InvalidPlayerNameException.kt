package ch.leadrian.samp.kamp.api.exception

class InvalidPlayerNameException(val name: String, message: String) : RuntimeException(message)
