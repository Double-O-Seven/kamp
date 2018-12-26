package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterRaceCheckpointListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerLeaveRaceCheckpointListener
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
) : CheckpointBase, AbstractDestroyable() {

    private val onPlayerEnterRaceCheckpointListeners: MutableList<OnPlayerEnterRaceCheckpointListener> = mutableListOf()

    private val onPlayerLeaveRaceCheckpointListeners: MutableList<OnPlayerLeaveRaceCheckpointListener> = mutableListOf()

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

    override fun contains(player: Player): Boolean = player.isInRaceCheckpoint(this)

    private fun update() {
        playerRegistry.getAll().filter { it.raceCheckpoint === this }.forEach {
            it.raceCheckpoint = this
        }
    }

    fun addOnPlayerEnterRaceCheckpointListener(listener: OnPlayerEnterRaceCheckpointListener) {
        onPlayerEnterRaceCheckpointListeners += listener
    }

    fun removeOnPlayerEnterRaceCheckpointListener(listener: OnPlayerEnterRaceCheckpointListener) {
        onPlayerEnterRaceCheckpointListeners -= listener
    }

    inline fun onEnter(crossinline onEnter: RaceCheckpoint.(Player) -> Unit): OnPlayerEnterRaceCheckpointListener {
        val listener = object : OnPlayerEnterRaceCheckpointListener {

            override fun onPlayerEnterRaceCheckpoint(player: Player) {
                player.raceCheckpoint?.let { onEnter.invoke(it, player) }
            }
        }
        addOnPlayerEnterRaceCheckpointListener(listener)
        return listener
    }

    internal fun onEnter(player: Player) {
        onPlayerEnterRaceCheckpointListeners.forEach { it.onPlayerEnterRaceCheckpoint(player) }
    }

    fun addOnPlayerLeaveRaceCheckpointListener(listener: OnPlayerLeaveRaceCheckpointListener) {
        onPlayerLeaveRaceCheckpointListeners += listener
    }

    fun removeOnPlayerLeaveRaceCheckpointListener(listener: OnPlayerLeaveRaceCheckpointListener) {
        onPlayerLeaveRaceCheckpointListeners -= listener
    }

    inline fun onLeave(crossinline onLeave: RaceCheckpoint.(Player) -> Unit): OnPlayerLeaveRaceCheckpointListener {
        val listener = object : OnPlayerLeaveRaceCheckpointListener {

            override fun onPlayerLeaveRaceCheckpoint(player: Player) {
                player.raceCheckpoint?.let { onLeave.invoke(it, player) }
            }
        }
        addOnPlayerLeaveRaceCheckpointListener(listener)
        return listener
    }

    internal fun onLeave(player: Player) {
        onPlayerLeaveRaceCheckpointListeners.forEach { it.onPlayerLeaveRaceCheckpoint(player) }
    }

    override fun onDestroy() {
        playerRegistry.getAll().forEach {
            if (it.isInRaceCheckpoint(this)) {
                onLeave(it)
                it.raceCheckpoint = null
            }
        }
    }
}