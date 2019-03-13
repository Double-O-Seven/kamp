package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.base.HasModelId
import ch.leadrian.samp.kamp.core.api.callback.OnActorStreamInReceiver
import ch.leadrian.samp.kamp.core.api.callback.OnActorStreamOutReceiver
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerGiveDamageActorReceiver
import ch.leadrian.samp.kamp.core.api.constants.BodyPart
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.Animation
import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionContainer
import ch.leadrian.samp.kamp.core.api.entity.extension.Extendable
import ch.leadrian.samp.kamp.core.api.entity.id.ActorId
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnActorStreamInReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnActorStreamOutReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerGiveDamageActorReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.entity.property.ActorAngleProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.ActorCoordinatesProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.ActorHealthProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.ActorPositionProperty

class Actor
internal constructor(
        val model: SkinModel,
        coordinates: Vector3D,
        angle: Float,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val onActorStreamInReceiver: OnActorStreamInReceiverDelegate = OnActorStreamInReceiverDelegate(),
        private val onActorStreamOutReceiver: OnActorStreamOutReceiverDelegate = OnActorStreamOutReceiverDelegate(),
        private val onPlayerGiveDamageActorReceiver: OnPlayerGiveDamageActorReceiverDelegate = OnPlayerGiveDamageActorReceiverDelegate()
) : Entity<ActorId>,
        AbstractDestroyable(),
        Extendable<Actor>,
        HasModelId by model,
        OnActorStreamInReceiver by onActorStreamInReceiver,
        OnActorStreamOutReceiver by onActorStreamOutReceiver,
        OnPlayerGiveDamageActorReceiver by onPlayerGiveDamageActorReceiver {

    override val extensions: EntityExtensionContainer<Actor> = EntityExtensionContainer(this)

    override val id: ActorId
        get() = requireNotDestroyed { field }

    var coordinates: Vector3D by ActorCoordinatesProperty(nativeFunctionExecutor)

    var angle: Float by ActorAngleProperty(nativeFunctionExecutor)

    var virtualWorldId: Int
        get() = nativeFunctionExecutor.getActorVirtualWorld(id.value)
        set(value) {
            nativeFunctionExecutor.setActorVirtualWorld(actorid = id.value, vworld = value)
        }

    var position: Position by ActorPositionProperty(nativeFunctionExecutor)

    var health: Float by ActorHealthProperty(nativeFunctionExecutor)

    var isInvulnerable: Boolean
        get() = nativeFunctionExecutor.isActorInvulnerable(id.value)
        set(value) {
            nativeFunctionExecutor.setActorInvulnerable(id.value, value)
        }

    init {
        val actorId = nativeFunctionExecutor.createActor(
                modelid = model.value,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z,
                rotation = angle
        )

        if (actorId == SAMPConstants.INVALID_ACTOR_ID) {
            throw CreationFailedException("Could not create actor")
        }

        id = ActorId.valueOf(actorId)
    }

    fun isStreamedIn(forPlayer: Player): Boolean =
            nativeFunctionExecutor.isActorStreamedIn(actorid = id.value, forplayerid = forPlayer.id.value)

    fun applyAnimation(
            animation: Animation,
            fDelta: Float,
            loop: Boolean,
            lockX: Boolean,
            lockY: Boolean,
            freeze: Boolean,
            time: Int
    ) {
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

    internal fun onStreamIn(player: Player) {
        onActorStreamInReceiver.onActorStreamIn(this, player)
    }

    internal fun onStreamOut(player: Player) {
        onActorStreamOutReceiver.onActorStreamOut(this, player)
    }

    internal fun onDamage(
            player: Player,
            amount: Float,
            weaponModel: WeaponModel,
            bodyPart: BodyPart
    ) {
        onPlayerGiveDamageActorReceiver.onPlayerGiveDamageActor(player, this, amount, weaponModel, bodyPart)
    }

    override fun onDestroy() {
        extensions.destroy()
        nativeFunctionExecutor.destroyActor(id.value)
    }
}