package ch.leadrian.samp.kamp.core.api.callback

import kotlin.reflect.KClass

open class CallbackListenerRegistry<T : Any>(val listenerClass: KClass<T>) {

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
        entries += Entry(listener as T, priority ?: getPriority(listener))
        isSorted = false
        return true
    }

    fun unregister(listener: Any) {
        entries.removeIf { it.listener === listener }
        isSorted = false
    }

    private fun getPriority(listener: Any): Int {
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

    private data class Entry<T>(val listener: T, val priority: Int)

}