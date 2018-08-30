package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.entity.Destroyable

internal abstract class AbstractDestroyable : Destroyable {

    protected open fun finalize() {
        if (!isDestroyed) {
            destroy()
        }
    }
}