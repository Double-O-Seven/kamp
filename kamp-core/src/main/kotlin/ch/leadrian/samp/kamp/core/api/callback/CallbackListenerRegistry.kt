package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.util.loggerFor
import kotlin.reflect.KClass

open class CallbackListenerRegistry<T : Any>(val listenerClass: KClass<T>) {

    private companion object {

        val log = loggerFor<CallbackListenerRegistry<*>>()
    }

    private val entries = mutableListOf<Entry<T>>()
    private var isSorted = false

    val listeners: Sequence<T>
        get() {
            if (!isSorted) {
                entries.sortByDescending { it.priority }
                isSorted = true
            }
            return entries.asSequence().map { it.listener }
        }

    fun register(listener: Any, priority: Int? = null): Boolean {
        if (!listenerClass.isInstance(listener)) return false

        entries.removeIf { it.listener === listener }
        @Suppress("UNCHECKED_CAST")
        val entry = Entry(listener as T, priority ?: getPriority(listener))
        entries += entry
        isSorted = false
        logRegistration(listener, entry.priority)
        return true
    }

    fun unregister(listener: Any): Boolean {
        val removed = entries.removeIf { it.listener === listener }
        if (removed) {
            isSorted = false
        }
        return removed
    }

    private fun getPriority(listener: Any): Int {
        val priority = listener::class.java.getAnnotation(Priority::class.java)?.takeIf {
            it.listenerClass == listenerClass
        }?.value
        return when {
            priority != null -> priority
            else -> {
                listener::class.java.getAnnotation(Priorities::class.java)?.value?.firstOrNull {
                    it.listenerClass == listenerClass
                }?.value ?: 0
            }
        }
    }

    private fun logRegistration(listener: Any, priority: Int) {
        log.info("Registered ${listener::class.java.name} as ${listenerClass.simpleName} with priority $priority")
    }

    private data class Entry<T>(val listener: T, val priority: Int)

}