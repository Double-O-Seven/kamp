package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.exception.AlreadyDestroyedException
import kotlin.reflect.full.cast

interface Destroyable {

    val isDestroyed: Boolean

    fun destroy()

    fun addOnDestroyListener(listener: OnDestroyListener)

    fun removeOnDestroyListener(listener: OnDestroyListener)

}

fun <T : Destroyable> T.requireNotDestroyed(): T {
    if (this.isDestroyed) {
        throw AlreadyDestroyedException("$this is already destroyed")
    }
    return this
}

inline fun <T : Destroyable, U> T.requireNotDestroyed(block: T.() -> U): U {
    requireNotDestroyed()
    return block(this)
}

inline fun <T : Destroyable, U : Any?> T.ifNotDestroyed(action: T.() -> U): U? {
    if (isDestroyed) {
        return null
    }
    return action.invoke(this)
}

inline fun <reified T : Destroyable> T.onDestroy(crossinline action: T.() -> Unit): OnDestroyListener {
    val listener = object : OnDestroyListener {
        override fun onDestroy(destroyable: Destroyable) {
            action(T::class.cast(destroyable))
        }
    }
    addOnDestroyListener(listener)
    return listener
}
