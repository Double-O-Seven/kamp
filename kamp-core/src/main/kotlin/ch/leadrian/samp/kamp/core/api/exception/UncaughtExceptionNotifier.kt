package ch.leadrian.samp.kamp.core.api.exception

interface UncaughtExceptionNotifier {

    fun notify(e: Exception)

}