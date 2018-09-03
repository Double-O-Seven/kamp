package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.RaceCheckpoint
import ch.leadrian.samp.kamp.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.runtime.entity.registry.PlayerRegistry

internal class RaceCheckpointImpl(
        coordinates: Vector3D,
        size: Float,
        type: RaceCheckpointType,
        nextCoordinates: Vector3D?,
        private val playerRegistry: PlayerRegistry
) : RaceCheckpoint {

    private val onEnterHandlers: MutableList<RaceCheckpoint.(Player) -> Unit> = mutableListOf()

    private val onLeaveHandlers: MutableList<RaceCheckpoint.(Player) -> Unit> = mutableListOf()

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

    override var type: RaceCheckpointType = type
        set(value) {
            requireNotDestroyed()
            field = value
            update()
        }

    override var nextCoordinates: Vector3D? = nextCoordinates?.toVector3D()
        set(value) {
            requireNotDestroyed()
            field = value?.toVector3D()
            update()
        }

    private fun update() {
        playerRegistry.getAll().filter { it.raceCheckpoint === this }.forEach {
            it.raceCheckpoint = this
        }
    }

    override fun onEnter(onEnter: RaceCheckpoint.(Player) -> Unit) {
        onEnterHandlers += onEnter
    }

    internal fun onEnter(player: Player) {
        requireNotDestroyed()
        onEnterHandlers.forEach { it.invoke(this, player) }
    }

    override fun onLeave(onLeave: RaceCheckpoint.(Player) -> Unit) {
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