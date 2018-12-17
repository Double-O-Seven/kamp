package ch.leadrian.samp.kamp.core.api.entity

abstract class AbstractDestroyable : Destroyable {

    private var onDestroyListeners: LinkedHashSet<OnDestroyListener> = LinkedHashSet()

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

        onDestroyListeners.forEach { it.onDestroy(this) }
        onDestroy()
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