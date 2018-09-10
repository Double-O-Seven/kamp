package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.data.Animation
import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.id.ActorId
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat

class Actor
internal constructor(
        model: SkinModel,
        coordinates: Vector3D,
        rotation: Float,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : Entity<ActorId>, AbstractDestroyable() {

    private val onDestroyHandlers: MutableList<Actor.() -> Unit> = mutableListOf()

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

    fun isStreamedIn(forPlayer: Player): Boolean =
            nativeFunctionExecutor.isActorStreamedIn(actorid = id.value, forplayerid = forPlayer.id.value)

    fun applyAnimation(animation: Animation, fDelta: Float, loop: Boolean, lockX: Boolean, lockY: Boolean, freeze: Boolean, time: Int) {
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

    fun clearAnimation() {
        nativeFunctionExecutor.clearActorAnimations(id.value)
    }

    var coordinates: Vector3D
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

    var angle: Float
        get() {
            val angle = ReferenceFloat()
            nativeFunctionExecutor.getActorFacingAngle(actorid = id.value, angle = angle)
            return angle.value
        }
        set(value) {
            nativeFunctionExecutor.setActorFacingAngle(actorid = id.value, angle = value)
        }

    var virtualWorldId: Int
        get() = nativeFunctionExecutor.getActorVirtualWorld(id.value)
        set(value) {
            nativeFunctionExecutor.setActorVirtualWorld(actorid = id.value, vworld = value)
        }

    var position: Position
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

    var health: Float
        get() {
            val health = ReferenceFloat()
            nativeFunctionExecutor.getActorHealth(actorid = id.value, health = health)
            return health.value
        }
        set(value) {
            nativeFunctionExecutor.setActorHealth(actorid = id.value, health = value)
        }

    var isInvulnerable: Boolean
        get() = nativeFunctionExecutor.isActorInvulnerable(id.value)
        set(value) {
            nativeFunctionExecutor.setActorInvulnerable(id.value, value)
        }

    internal fun onDestroy(onDestroy: Actor.() -> Unit) {
        onDestroyHandlers += onDestroy
    }

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) return

        onDestroyHandlers.forEach { it.invoke(this) }
        nativeFunctionExecutor.destroyActor(id.value)
        isDestroyed = true
    }
}