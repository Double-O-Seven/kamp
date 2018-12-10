package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry

class Checkpoint
internal constructor(
        coordinates: Vector3D,
        size: Float,
        private val playerRegistry: PlayerRegistry
) : CheckpointBase {

    private val onEnterHandlers: MutableList<Checkpoint.(Player) -> Unit> = mutableListOf()

    private val onLeaveHandlers: MutableList<Checkpoint.(Player) -> Unit> = mutableListOf()

    override var coordinates: Vector3D = coordinates.toVector3D()
        set(value) {
            requireNotDestroyed()
            field = value.toVector3D()
            update()
        }

    override var size: Float = size
        set(value) {
            requireNotDestroyed()
            field = value
            update()
        }

    override fun contains(player: Player): Boolean = player.isInCheckpoint(this)

    private fun update() {
        playerRegistry.getAll().filter { it.checkpoint === this }.forEach {
            it.checkpoint = this
        }
    }

    fun onEnter(onEnter: Checkpoint.(Player) -> Unit) {
        onEnterHandlers += onEnter
    }

    internal fun onEnter(player: Player) {
        requireNotDestroyed()
        onEnterHandlers.forEach { it.invoke(this, player) }
    }

    fun onLeave(onLeave: Checkpoint.(Player) -> Unit) {
        onLeaveHandlers += onLeave
    }

    internal fun onLeave(player: Player) {
        requireNotDestroyed()
        onLeaveHandlers.forEach { it.invoke(this, player) }
    }

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) return

        playerRegistry.getAll().forEach {
            if (it.isInCheckpoint(this)) {
                onLeave(it)
                it.checkpoint = null
            }
        }
        isDestroyed = true
    }
}