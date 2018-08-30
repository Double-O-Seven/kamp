package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.exception.AlreadyDestroyedException

interface Destroyable {

    val isDestroyed: Boolean

    fun destroy()

}

fun <T : Destroyable> T.requireNotDestroyed(): T {
    if (this.isDestroyed) throw AlreadyDestroyedException("$this is already destroyed")
    return this
}

inline fun <T : Destroyable, U> T.requireNotDestroyed(block: T.() -> U): U {
    requireNotDestroyed()
    return block(this)
}
