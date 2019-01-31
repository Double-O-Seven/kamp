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

    /**
     * Registers [listener] as listener for all known callbacks.
     *
     * If [listener] is already registered, it will be re-registered with new [priority].
     *
     * If [priority] is not null, [listener] will be registered with priority [priority],
     * else, if any [Priority] or [Priorities] annotation is present, the priority will be derived from those,
     * else, the default priority of 0 will be used.
     *
     * @return true, if [listener] is an instance of [T], else false
     */
    fun register(listener: Any, priority: Int? = null): Boolean {
        if (!listenerClass.isInstance(listener)) return false

        entries.removeIf { it.listener === listener }
        @Suppress("UNCHECKED_CAST")
        val entry = Entry(listener as T, priority ?: getPriority(listener))
        entries += entry
        isSorted = false
        return true
    }

    /**
     * Unregisters [listener] as listener for all known callbacks.
     *
     * @return true if [listener] was registered before, else false
     */
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

    private data class Entry<T>(val listener: T, val priority: Int)

}