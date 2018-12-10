package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry

class RaceCheckpoint
internal constructor(
        coordinates: Vector3D,
        size: Float,
        type: RaceCheckpointType,
        nextCoordinates: Vector3D?,
        private val playerRegistry: PlayerRegistry
) : Destroyable {

    private val onEnterHandlers: MutableList<RaceCheckpoint.(Player) -> Unit> = mutableListOf()

    private val onLeaveHandlers: MutableList<RaceCheckpoint.(Player) -> Unit> = mutableListOf()

    var coordinates: Vector3D = coordinates.toVector3D()
        set(value) {
            requireNotDestroyed()
            field = value.toVector3D()
            update()
        }

    var size: Float = size
        set(value) {
            requireNotDestroyed()
            field = value
            update()
        }

    var type: RaceCheckpointType = type
        set(value) {
            requireNotDestroyed()
            field = value
            update()
        }

    var nextCoordinates: Vector3D? = nextCoordinates?.toVector3D()
        set(value) {
            requireNotDestroyed()
            field = value?.toVector3D()
            update()
        }

    operator fun contains(player: Player): Boolean = player.isInRaceCheckpoint(this)

    private fun update() {
        playerRegistry.getAll().filter { it.raceCheckpoint === this }.forEach {
            it.raceCheckpoint = this
        }
    }

    fun onEnter(onEnter: RaceCheckpoint.(Player) -> Unit) {
        onEnterHandlers += onEnter
    }

    internal fun onEnter(player: Player) {
        requireNotDestroyed()
        onEnterHandlers.forEach { it.invoke(this, player) }
    }

    fun onLeave(onLeave: RaceCheckpoint.(Player) -> Unit) {
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
            if (it.isInRaceCheckpoint(this)) {
                onLeave(it)
                it.raceCheckpoint = null
            }
        }
        isDestroyed = true
    }
}