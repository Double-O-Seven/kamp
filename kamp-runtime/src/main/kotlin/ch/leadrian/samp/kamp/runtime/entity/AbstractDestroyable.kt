package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.entity.Destroyable

internal abstract class AbstractDestroyable : Destroyable {

    protected fun finalize() {
        if (!isDestroyed) {
            destroy()
        }
    }
}