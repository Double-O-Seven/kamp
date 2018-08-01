package ch.leadrian.samp.kamp.api.service

import ch.leadrian.samp.kamp.api.constants.SkinModel
import ch.leadrian.samp.kamp.api.data.AngledLocation
import ch.leadrian.samp.kamp.api.data.Position
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.Actor
import ch.leadrian.samp.kamp.api.entity.id.ActorId

interface ActorService {

    fun isValid(actorId: ActorId): Boolean

    fun createActor(skinModel: SkinModel, position: Position)

    fun createActor(skinModel: SkinModel, coordinates: Vector3D, angle: Float)

    fun createActor(skinModel: SkinModel, angledLocation: AngledLocation)

    fun getActor(actorId: ActorId): Actor

    fun getAllActors(): List<Actor>

    fun getPoolSize(): Int

}