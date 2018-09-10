package ch.leadrian.samp.kamp.core.api.entity

abstract class AbstractDestroyable : Destroyable {

    protected open fun finalize() {
        if (!isDestroyed) {
            destroy()
        }
    }
}