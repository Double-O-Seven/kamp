package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.callback.OnActorStreamInListener
import ch.leadrian.samp.kamp.core.api.callback.OnActorStreamOutListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerGiveDamageActorListener
import ch.leadrian.samp.kamp.core.api.constants.BodyPart
import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.Animation
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionContainer
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.core.api.service.ActorService
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerDamageStreamableActorReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableActorStreamInReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableActorStreamOutReceiver
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableActor
import ch.leadrian.samp.kamp.streamer.runtime.ActorStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerDamageStreamableActorHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerDamageStreamableActorReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableActorStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableActorStreamInReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableActorStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableActorStreamOutReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.util.toRect3d
import com.conversantmedia.util.collection.geometry.Rect3d

internal class StreamableActorImpl(
        model: SkinModel,
        coordinates: Vector3D,
        angle: Float,
        isInvulnerable: Boolean,
        virtualWorldId: Int,
        override var interiorIds: MutableSet<Int>,
        override val streamDistance: Float,
        override val priority: Int,
        private val actorService: ActorService,
        private val onStreamableActorStreamInHandler: OnStreamableActorStreamInHandler,
        private val onStreamableActorStreamOutHandler: OnStreamableActorStreamOutHandler,
        private val onPlayerDamageStreamableActorHandler: OnPlayerDamageStreamableActorHandler,
        private val actorStreamer: ActorStreamer,
        private val onStreamableActorStreamInReceiver: OnStreamableActorStreamInReceiverDelegate = OnStreamableActorStreamInReceiverDelegate(),
        private val onStreamableActorStreamOutReceiver: OnStreamableActorStreamOutReceiverDelegate = OnStreamableActorStreamOutReceiverDelegate(),
        private val onPlayerDamageStreamableActorReceiver: OnPlayerDamageStreamableActorReceiverDelegate = OnPlayerDamageStreamableActorReceiverDelegate()
) :
        CoordinatesBasedGlobalStreamable<StreamableActorImpl, Rect3d>(),
        StreamableActor,
        DistanceBasedGlobalStreamable,
        OnStreamableActorStreamInReceiver by onStreamableActorStreamInReceiver,
        OnStreamableActorStreamOutReceiver by onStreamableActorStreamOutReceiver,
        OnPlayerDamageStreamableActorReceiver by onPlayerDamageStreamableActorReceiver,
        OnActorStreamInListener,
        OnActorStreamOutListener,
        OnPlayerGiveDamageActorListener {

    private var actor: Actor? = null

    override val extensions: EntityExtensionContainer<StreamableActor> = EntityExtensionContainer(this)

    override var model: SkinModel = model
        set(value) {
            field = value
            if (actor != null) {
                destroyActor()
                createActor()
            }
        }

    override var isInvulnerable: Boolean = isInvulnerable
        set(value) {
            field = value
            actor?.isInvulnerable = isInvulnerable
        }

    override var coordinates: Vector3D = coordinates.toVector3D()
        set(value) {
            field = value.toVector3D()
            actor?.coordinates = field
            actorStreamer.onBoundingBoxChange(this)
        }

    override var angle: Float = angle
        set(value) {
            field = value
            actor?.angle = value
        }

    override var position: Position
        get() = positionOf(coordinates, angle)
        set(value) {
            coordinates = value
            angle = value.angle
        }

    override var virtualWorldId: Int = virtualWorldId
        set(value) {
            field = value
            actor?.virtualWorldId = value
        }

    override var health: Float = 100f
        get() = actor?.health ?: field
        set(value) {
            field = value
            actor?.health = value
        }

    override val isStreamedIn: Boolean
        get() = actor != null

    override fun applyAnimation(
            animation: Animation,
            fDelta: Float,
            loop: Boolean,
            lockX: Boolean,
            lockY: Boolean,
            freeze: Boolean,
            time: Int
    ) {
        actor?.applyAnimation(
                animation = animation,
                fDelta = fDelta,
                loop = loop,
                lockX = lockX,
                lockY = lockY,
                freeze = freeze,
                time = time
        )
    }

    override fun clearAnimation() {
        actor?.clearAnimation()
    }

    override fun distanceTo(location: Location): Float {
        return when {
            virtualWorldId != location.virtualWorldId -> Float.MAX_VALUE
            interiorIds.isNotEmpty() && location.interiorId !in interiorIds -> Float.MAX_VALUE
            else -> coordinates.distanceTo(location)
        }
    }

    override fun onStreamIn() {
        requireNotDestroyed()
        if (isStreamedIn) {
            throw IllegalStateException("Actor is already streamed in")
        }
        createActor()
    }

    override fun onStreamOut() {
        requireNotDestroyed()
        if (!isStreamedIn) {
            throw IllegalStateException("Actor was not streamed in")
        }
        destroyActor()
    }

    override fun onActorStreamIn(actor: Actor, forPlayer: Player) {
        requireStreamedInActor(actor)
        onStreamableActorStreamInReceiver.onStreamableActorStreamIn(this, forPlayer)
        onStreamableActorStreamInHandler.onStreamableActorStreamIn(this, forPlayer)
    }

    override fun onActorStreamOut(actor: Actor, forPlayer: Player) {
        requireStreamedInActor(actor)
        onStreamableActorStreamOutReceiver.onStreamableActorStreamOut(this, forPlayer)
        onStreamableActorStreamOutHandler.onStreamableActorStreamOut(this, forPlayer)
    }

    override fun onPlayerGiveDamageActor(
            player: Player,
            actor: Actor,
            amount: Float,
            weaponModel: WeaponModel,
            bodyPart: BodyPart
    ) {
        requireStreamedInActor(actor)
        onPlayerDamageStreamableActorReceiver.onPlayerDamageStreamableActor(player, this, amount, weaponModel, bodyPart)
        onPlayerDamageStreamableActorHandler.onPlayerDamageStreamableActor(player, this, amount, weaponModel, bodyPart)
    }

    private fun requireStreamedInActor(actor: Actor) {
        if (actor != this.actor) {
            throw IllegalStateException("Actor is not the streamed in actor")
        }
    }

    override fun getBoundingBox(): Rect3d = coordinates.toRect3d(streamDistance)

    override fun onDestroy() {
        extensions.destroy()
        destroyActor()
    }

    private fun createActor() {
        val actor = actorService.createActor(model, coordinates, angle)
        actor.isInvulnerable = isInvulnerable
        actor.health = health
        actor.virtualWorldId = virtualWorldId
        actor.addOnActorStreamInListener(this)
        actor.addOnActorStreamOutListener(this)
        actor.addOnPlayerGiveDamageActorListener(this)
        this.actor = actor
    }

    private fun destroyActor() {
        actor?.apply {
            this@StreamableActorImpl.health = health
            removeOnActorStreamInListener(this@StreamableActorImpl)
            removeOnActorStreamOutListener(this@StreamableActorImpl)
            removeOnPlayerGiveDamageActorListener(this@StreamableActorImpl)
            destroy()
        }
        actor = null
    }

}