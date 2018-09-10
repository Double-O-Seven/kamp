package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.id.ActorId

interface ActorService {

    fun isValid(actorId: ActorId): Boolean

    fun createActor(skinModel: ch.leadrian.samp.kamp.core.api.constants.SkinModel, position: Position): Actor

    fun createActor(skinModel: ch.leadrian.samp.kamp.core.api.constants.SkinModel, coordinates: Vector3D, angle: Float): Actor

    fun createActor(skinModel: ch.leadrian.samp.kamp.core.api.constants.SkinModel, angledLocation: ch.leadrian.samp.kamp.core.api.data.AngledLocation): Actor

    fun getActor(actorId: ActorId): Actor

    fun getAllActors(): List<Actor>

    fun getPoolSize(): Int

}