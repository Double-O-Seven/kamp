package ch.leadrian.samp.kamp.core.runtime.entity

import ch.leadrian.samp.kamp.core.api.entity.Destroyable

internal abstract class AbstractDestroyable : Destroyable {

    protected open fun finalize() {
        if (!isDestroyed) {
            destroy()
        }
    }
}