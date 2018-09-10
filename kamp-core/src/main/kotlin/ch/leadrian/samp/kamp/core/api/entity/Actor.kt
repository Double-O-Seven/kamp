package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.id.ActorId

interface Actor : Destroyable, Entity<ActorId> {

    override val id: ActorId

    fun isStreamedIn(forPlayer: Player): Boolean

    var virtualWorldId: Int

    fun applyAnimation(
            animation: ch.leadrian.samp.kamp.core.api.data.Animation,
            fDelta: Float,
            loop: Boolean,
            lockX: Boolean,
            lockY: Boolean,
            freeze: Boolean,
            time: Int
    )

    fun clearAnimation()

    var coordinates: Vector3D

    var position: Position

    var angle: Float

    var health: Float

    var isInvulnerable: Boolean
}