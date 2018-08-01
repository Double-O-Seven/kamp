package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.data.Animation
import ch.leadrian.samp.kamp.api.data.Position
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.id.ActorId

interface Actor : Destroyable {

    val id: ActorId

    fun isStreamedIn(forPlayer: Player)

    var virtualWorld: Int

    fun applyAnimation(
            animation: Animation,
            fDelta: Float,
            loop: Boolean,
            lockX: Boolean,
            lockY: Boolean,
            freeze: Boolean,
            time: Int
    )

    fun clearAnimation()

    var position3D: Vector3D

    var position: Position

    var angle: Float

    var health: Float

    var isInvulnerable: Boolean
}