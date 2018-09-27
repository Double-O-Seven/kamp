package ch.leadrian.samp.kamp.core.runtime.service

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterCheckpointListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerLeaveCheckpointListener
import ch.leadrian.samp.kamp.core.api.data.Sphere
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Checkpoint
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.CheckpointService
import ch.leadrian.samp.kamp.core.runtime.entity.factory.CheckpointFactory
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CheckpointServiceImpl
@Inject
constructor(
        private val checkpointFactory: CheckpointFactory,
        private val callbackListenerManager: CallbackListenerManager
) : CheckpointService, OnPlayerEnterCheckpointListener, OnPlayerLeaveCheckpointListener {

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onPlayerEnterCheckpoint(player: Player) {
        player.checkpoint?.onEnter(player)
    }

    override fun onPlayerLeaveCheckpoint(player: Player) {
        player.checkpoint?.onLeave(player)
    }

    override fun createCheckpoint(coordinates: Vector3D, size: Float): Checkpoint =
            checkpointFactory.create(coordinates, size)

    override fun createCheckpoint(sphere: Sphere): Checkpoint =
            checkpointFactory.create(vector3DOf(x = sphere.x, y = sphere.y, z = sphere.z), sphere.radius)
}