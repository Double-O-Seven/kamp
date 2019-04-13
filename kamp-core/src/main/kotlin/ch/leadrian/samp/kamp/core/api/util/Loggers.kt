package ch.leadrian.samp.kamp.core.api.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T : Any> loggerFor(): Logger = LoggerFactory.getLogger(T::class.java)

inline fun <reified T : Any> T.logger(): Logger {
    val targetClass = T::class.takeIf { it.isCompanion }?.java?.declaringClass ?: T::class.java
    return LoggerFactory.getLogger(targetClass)
}
