package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterCheckpointListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerLeaveCheckpointListener
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry

class Checkpoint
internal constructor(
        coordinates: Vector3D,
        size: Float,
        private val playerRegistry: PlayerRegistry
) : CheckpointBase, AbstractDestroyable() {

    private val onPlayerEnterCheckpointListeners = LinkedHashSet<OnPlayerEnterCheckpointListener>()

    private val onPlayerLeaveCheckpointListeners = LinkedHashSet<OnPlayerLeaveCheckpointListener>()

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

    fun addOnPlayerEnterCheckpointListener(listener: OnPlayerEnterCheckpointListener) {
        onPlayerEnterCheckpointListeners += listener
    }

    fun removeOnPlayerEnterCheckpointListener(listener: OnPlayerEnterCheckpointListener) {
        onPlayerEnterCheckpointListeners -= listener
    }

    inline fun onEnter(crossinline onEnter: Checkpoint.(Player) -> Unit): OnPlayerEnterCheckpointListener {
        val listener = object : OnPlayerEnterCheckpointListener {

            override fun onPlayerEnterCheckpoint(player: Player) {
                player.checkpoint?.let { onEnter.invoke(it, player) }
            }
        }
        addOnPlayerEnterCheckpointListener(listener)
        return listener
    }

    internal fun onEnter(player: Player) {
        onPlayerEnterCheckpointListeners.forEach { it.onPlayerEnterCheckpoint(player) }
    }

    fun addOnPlayerLeaveCheckpointListener(listener: OnPlayerLeaveCheckpointListener) {
        onPlayerLeaveCheckpointListeners += listener
    }

    fun removeOnPlayerLeaveCheckpointListener(listener: OnPlayerLeaveCheckpointListener) {
        onPlayerLeaveCheckpointListeners -= listener
    }

    inline fun onLeave(crossinline onLeave: Checkpoint.(Player) -> Unit): OnPlayerLeaveCheckpointListener {
        val listener = object : OnPlayerLeaveCheckpointListener {

            override fun onPlayerLeaveCheckpoint(player: Player) {
                player.checkpoint?.let { onLeave.invoke(it, player) }
            }
        }
        addOnPlayerLeaveCheckpointListener(listener)
        return listener
    }

    internal fun onLeave(player: Player) {
        onPlayerLeaveCheckpointListeners.forEach { it.onPlayerLeaveCheckpoint(player) }
    }

    override fun onDestroy() {
        playerRegistry.getAll().forEach { player ->
            if (player in this) {
                onLeave(player)
                player.checkpoint = null
            }
        }
    }
}