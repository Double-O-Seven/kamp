package ch.leadrian.samp.kamp.core.api.entity

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