package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.data.AngledLocation
import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.id.ActorId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.factory.ActorFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.ActorRegistry
import javax.inject.Inject

class ActorService
@Inject
internal constructor(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val actorRegistry: ActorRegistry,
        private val actorFactory: ActorFactory
) {

    fun isValid(actorId: ActorId): Boolean = nativeFunctionExecutor.isValidActor(actorId.value)

    fun createActor(skinModel: SkinModel, position: Position): Actor =
            actorFactory.create(skinModel, position, position.angle)

    fun createActor(skinModel: SkinModel, coordinates: Vector3D, angle: Float): Actor =
            actorFactory.create(skinModel, coordinates, angle)

    fun createActor(skinModel: SkinModel, angledLocation: AngledLocation): Actor =
            actorFactory.create(skinModel, angledLocation, angledLocation.angle).apply {
                virtualWorldId = angledLocation.virtualWorldId
            }

    fun getActor(actorId: ActorId): Actor {
        return actorRegistry[actorId] ?: throw NoSuchEntityException("No actor with ID ${actorId.value}")
    }

    fun getAllActors(): List<Actor> = actorRegistry.getAll()

    fun getPoolSize(): Int = nativeFunctionExecutor.getActorPoolSize()

}