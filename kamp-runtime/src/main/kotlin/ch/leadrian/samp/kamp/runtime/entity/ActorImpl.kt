package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.constants.SkinModel
import ch.leadrian.samp.kamp.api.data.*
import ch.leadrian.samp.kamp.api.entity.Actor
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.id.ActorId
import ch.leadrian.samp.kamp.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.types.ReferenceFloat

internal class ActorImpl(
        model: SkinModel,
        coordinates: Vector3D,
        rotation: Float,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : Actor {

    override val id: ActorId
        get() = requireNotDestroyed { field }

    init {
        val actorId = nativeFunctionExecutor.createActor(
                modelid = model.value,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z,
                rotation = rotation
        )

        if (actorId == SAMPConstants.INVALID_ACTOR_ID) {
            throw CreationFailedException("Could not create actor")
        }

        id = ActorId.valueOf(actorId)
    }

    override fun isStreamedIn(forPlayer: Player): Boolean =
            nativeFunctionExecutor.isActorStreamedIn(actorid = id.value, forplayerid = forPlayer.id.value)

    override fun applyAnimation(animation: Animation, fDelta: Float, loop: Boolean, lockX: Boolean, lockY: Boolean, freeze: Boolean, time: Int) {
        nativeFunctionExecutor.applyActorAnimation(
                actorid = id.value,
                animlib = animation.library,
                animname = animation.animationName,
                fDelta = fDelta,
                loop = loop,
                lockx = lockX,
                locky = lockY,
                freeze = freeze,
                time = time
        )
    }

    override fun clearAnimation() {
        nativeFunctionExecutor.clearActorAnimations(id.value)
    }

    override var coordinates: Vector3D
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getActorPos(actorid = id.value, x = x, y = y, z = z)
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }
        set(value) {
            nativeFunctionExecutor.setActorPos(actorid = id.value, x = value.x, y = value.y, z = value.z)
        }

    override var angle: Float
        get() {
            val angle = ReferenceFloat()
            nativeFunctionExecutor.getActorFacingAngle(actorid = id.value, angle = angle)
            return angle.value
        }
        set(value) {
            nativeFunctionExecutor.setActorFacingAngle(actorid = id.value, angle = value)
        }

    override var virtualWorldId: Int
        get() = nativeFunctionExecutor.getActorVirtualWorld(id.value)
        set(value) {
            nativeFunctionExecutor.setActorVirtualWorld(actorid = id.value, vworld = value)
        }

    override var position: Position
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getActorPos(actorid = id.value, x = x, y = y, z = z)
            return positionOf(x = x.value, y = y.value, z = z.value, angle = angle)
        }
        set(value) {
            coordinates = value
            angle = value.angle
        }

    override var health: Float
        get() {
            val health = ReferenceFloat()
            nativeFunctionExecutor.getActorHealth(actorid = id.value, health = health)
            return health.value
        }
        set(value) {
            nativeFunctionExecutor.setActorHealth(actorid = id.value, health = value)
        }

    override var isInvulnerable: Boolean
        get() = nativeFunctionExecutor.isActorInvulnerable(id.value)
        set(value) {
            nativeFunctionExecutor.setActorInvulnerable(id.value, value)
        }

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) return

        nativeFunctionExecutor.destroyActor(id.value)
        isDestroyed = true
    }
}