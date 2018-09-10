package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.ActorRegistry
import javax.inject.Inject

internal class ActorFactory
@Inject
constructor(
        private val actorRegistry: ActorRegistry,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) {

    fun create(model: SkinModel, coordinates: Vector3D, rotation: Float): Actor {
        val actor = Actor(model, coordinates, rotation, nativeFunctionExecutor)
        actorRegistry.register(actor)
        actor.onDestroy { actorRegistry.unregister(this) }
        return actor
    }

}