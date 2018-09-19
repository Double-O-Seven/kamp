package ch.leadrian.samp.kamp.core.api.callback

import java.util.*
import kotlin.reflect.KClass

open class CallbackListenerRegistry<T : Any>(private val listenerClass: KClass<T>) {

    private val entries = TreeSet<Entry<T>>()

    fun getListeners(): Sequence<T> = entries.asSequence().map { it.listener }

    fun register(listener: T, priority: Int = getPriority(listener)) {
        entries.removeIf { it.listener == listener }
        entries += Entry(listener, priority)
    }

    fun unregister(listener: T) {
        entries.removeIf { it.listener == listener }
    }

    private fun getPriority(listener: T): Int {
        val priority = listener::class.java.getAnnotation(Priority::class.java)?.value
        return when {
            priority != null -> priority
            else -> {
                listener::class.java.getAnnotation(Priorities::class.java)?.value?.firstOrNull {
                    it.listenerClass == listenerClass
                }?.value ?: 0
            }
        }
    }

    private data class Entry<T>(val listener: T, val priority: Int) : Comparable<Entry<T>> {

        override fun compareTo(other: Entry<T>): Int = other.priority - this.priority

    }

}