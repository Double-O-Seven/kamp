package ch.leadrian.samp.kamp.core.api.exception

import org.slf4j.Logger

interface SafeCaller {

    val uncaughtExceptionNotifier: UncaughtExceptionNotifier?

    val log: Logger

    @JvmDefault
    fun handleException(exception: Exception) {
        log.error("Uncaught exception", exception)
        try {
            uncaughtExceptionNotifier?.notify(exception)
        } catch (e: Exception) {
            log.error("Exception while notifying {} about exception", uncaughtExceptionNotifier, e)
        }
    }

}

inline fun <T : SafeCaller, U : Any> T.tryAndCatch(block: T.() -> U?): U? {
    return try {
        block()
    } catch (e: Exception) {
        handleException(e)
        null
    }
}