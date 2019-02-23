package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.exception.AlreadyDestroyedException
import kotlin.reflect.full.cast

interface Destroyable {

    /**
     * @return true if the entity was destroyed, else false
     */
    val isDestroyed: Boolean

    /**
     * Should do all required clean-up tasks such as destroying the SA-MP server primitive entity or unregistering
     * in it's [ch.leadrian.samp.kamp.core.runtime.entity.registry.EntityRegistry].
     *
     * Any clean-up action should only be done in the first call.
     * Additionally, [OnDestroyListener]s should only be notified during the first call.
     */
    fun destroy()

    /**
     * Adds an [OnDestroyListener] that will be called during the first execution of [destroy].
     */
    fun addOnDestroyListener(listener: OnDestroyListener)

    /**
     * Removes an [OnDestroyListener] again. If [destroy] was not yet called, the [listener] will not be called when [destroy] is called.
     */
    fun removeOnDestroyListener(listener: OnDestroyListener)

}

/**
 * Throws an [AlreadyDestroyedException] if [this] was already destroyed.
 *
 * @return [this]
 * @throws [AlreadyDestroyedException]
 */
fun <T : Destroyable> T.requireNotDestroyed(): T {
    if (this.isDestroyed) {
        throw AlreadyDestroyedException("$this is already destroyed")
    }
    return this
}

/**
 * Throws an [AlreadyDestroyedException] if [this] was already destroyed.
 * If [this] was not destroyed [block] will be invoked and it's result returned.
 *
 * @return the result of [block]
 * @throws [AlreadyDestroyedException]
 */
inline fun <T : Destroyable, U> T.requireNotDestroyed(block: T.() -> U): U {
    requireNotDestroyed()
    return block(this)
}

/**
 * Executes [action] if [this] was not yet destroyed and returns it's result.
 * Else null is returned.
 */
inline fun <T : Destroyable, U : Any?> T.ifNotDestroyed(action: T.() -> U): U? {
    if (isDestroyed) {
        return null
    }
    return action.invoke(this)
}

/**
 * Registers and anonymous [OnDestroyListener] executing [action] when [Destroyable.destroy] is called.
 *
 * Example:
 * ```kotlin
 * vehicle.onDestroy {
 *     log.info("Vehicle ${id.value} was destroyed")
 * }
 * ```
 *
 * @return the anonymous [OnDestroyListener] in order to be unable to unregister it again using [Destroyable.removeOnDestroyListener]
 */
inline fun <reified T : Destroyable> T.onDestroy(crossinline action: T.() -> Unit): OnDestroyListener {
    val listener = object : OnDestroyListener {
        override fun onDestroy(destroyable: Destroyable) {
            action(T::class.cast(destroyable))
        }
    }
    addOnDestroyListener(listener)
    return listener
}
