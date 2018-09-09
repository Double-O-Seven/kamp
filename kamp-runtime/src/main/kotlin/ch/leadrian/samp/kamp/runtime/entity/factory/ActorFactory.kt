package ch.leadrian.samp.kamp.runtime.entity.factory

import ch.leadrian.samp.kamp.api.constants.SkinModel
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.ActorImpl
import ch.leadrian.samp.kamp.runtime.entity.registry.ActorRegistry
import javax.inject.Inject

internal class ActorFactory
@Inject
constructor(
        private val actorRegistry: ActorRegistry,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) {

    fun create(model: SkinModel, coordinates: Vector3D, rotation: Float): ActorImpl {
        val actor = ActorImpl(model, coordinates, rotation, nativeFunctionExecutor)
        actorRegistry.register(actor)
        actor.onDestroy { actorRegistry.unregister(this) }
        return actor
    }

}