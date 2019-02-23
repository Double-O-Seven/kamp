package ch.leadrian.samp.kamp.core.api.entity

/**
 * Base class for [Destroyable]s. Any concrete implementation should use this class as base at some point.
 * [destroy] is called in [finalize] in order to clean up any SA-MP server entities like vehicles for example,
 * in case it's Kamp wrapper object is garbage-collected.
 */
abstract class AbstractDestroyable : Destroyable {

    private val onDestroyListeners = LinkedHashSet<OnDestroyListener>()

    override var isDestroyed: Boolean = false
        protected set

    override fun addOnDestroyListener(listener: OnDestroyListener) {
        onDestroyListeners.add(listener)
    }

    override fun removeOnDestroyListener(listener: OnDestroyListener) {
        onDestroyListeners.remove(listener)
    }

    final override fun destroy() {
        if (isDestroyed) {
            return
        }

        onDestroy()
        onDestroyListeners.forEach { it.onDestroy(this) }
        isDestroyed = true
    }

    protected abstract fun onDestroy()

    protected fun callOnDestroyListeners() {
        onDestroyListeners.forEach { it.onDestroy(this) }
    }

    protected open fun finalize() {
        destroy()
    }
}