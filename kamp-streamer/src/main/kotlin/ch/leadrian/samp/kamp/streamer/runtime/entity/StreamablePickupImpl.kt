package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerPickUpPickupListener
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionContainer
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.core.api.service.PickupService
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerPickUpStreamablePickupReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamablePickupStreamInReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamablePickupStreamOutReceiver
import ch.leadrian.samp.kamp.streamer.api.entity.StreamablePickup
import ch.leadrian.samp.kamp.streamer.runtime.PickupStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerPickUpStreamablePickupHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerPickUpStreamablePickupReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamablePickupStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamablePickupStreamInReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamablePickupStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamablePickupStreamOutReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.util.toRect3d
import com.conversantmedia.util.collection.geometry.Rect3d

internal class StreamablePickupImpl(
        modelId: Int,
        coordinates: Vector3D,
        type: Int,
        virtualWorldId: Int?,
        override var interiorIds: MutableSet<Int>,
        override val streamDistance: Float,
        override val priority: Int,
        private val pickupStreamer: PickupStreamer,
        private val pickupService: PickupService,
        private val onPlayerPickUpStreamablePickupHandler: OnPlayerPickUpStreamablePickupHandler,
        private val onStreamablePickupStreamInHandler: OnStreamablePickupStreamInHandler,
        private val onStreamablePickupStreamOutHandler: OnStreamablePickupStreamOutHandler,
        private val onPlayerPickUpStreamablePickupReceiver: OnPlayerPickUpStreamablePickupReceiverDelegate = OnPlayerPickUpStreamablePickupReceiverDelegate(),
        private val onStreamablePickupStreamInReceiver: OnStreamablePickupStreamInReceiverDelegate = OnStreamablePickupStreamInReceiverDelegate(),
        private val onStreamablePickupStreamOutReceiver: OnStreamablePickupStreamOutReceiverDelegate = OnStreamablePickupStreamOutReceiverDelegate()
) :
        CoordinatesBasedGlobalStreamable<StreamablePickupImpl, Rect3d>(),
        StreamablePickup,
        DistanceBasedGlobalStreamable,
        OnPlayerPickUpPickupListener,
        OnPlayerPickUpStreamablePickupReceiver by onPlayerPickUpStreamablePickupReceiver,
        OnStreamablePickupStreamInReceiver by onStreamablePickupStreamInReceiver,
        OnStreamablePickupStreamOutReceiver by onStreamablePickupStreamOutReceiver {

    private var pickup: Pickup? = null

    override val extensions: EntityExtensionContainer<StreamablePickup> = EntityExtensionContainer(this)

    override val isStreamedIn: Boolean
        get() = pickup != null

    override var coordinates: Vector3D = coordinates.toVector3D()
        set(value) {
            field = value.toVector3D()
            updatePickup()
            pickupStreamer.onBoundingBoxChange(this)
        }

    override var modelId: Int = modelId
        set(value) {
            field = value
            updatePickup()
        }

    override var type: Int = type
        set(value) {
            field = value
            updatePickup()
        }

    override var virtualWorldId: Int? = virtualWorldId
        set(value) {
            field = value
            updatePickup()
        }

    override val boundingBox: Rect3d
        get() = coordinates.toRect3d(streamDistance)

    override fun distanceTo(location: Location): Float {
        return when {
            virtualWorldId != null && location.virtualWorldId != virtualWorldId -> Float.MAX_VALUE
            interiorIds.isNotEmpty() && location.interiorId !in interiorIds -> Float.MAX_VALUE
            else -> coordinates.distanceTo(location)
        }
    }

    override fun onStreamIn() {
        requireNotDestroyed()
        if (isStreamedIn) {
            throw IllegalStateException("Pickup is already streamed in")
        }
        createPickup()
        onStreamablePickupStreamInReceiver.onStreamablePickupStreamIn(this)
        onStreamablePickupStreamInHandler.onStreamablePickupStreamIn(this)
    }

    override fun onStreamOut() {
        requireNotDestroyed()
        if (!isStreamedIn) {
            throw IllegalStateException("Pickup was not streamed in")
        }
        destroyPickup()
        onStreamablePickupStreamOutReceiver.onStreamablePickupStreamOut(this)
        onStreamablePickupStreamOutHandler.onStreamablePickupStreamOut(this)
    }

    override fun onPlayerPickUpPickup(player: Player, pickup: Pickup) {
        if (pickup != this.pickup) {
            throw IllegalStateException("Pickup is not the streamed in pickup")
        }
        onPlayerPickUpStreamablePickupReceiver.onPlayerPickUpStreamablePickup(player, this)
        onPlayerPickUpStreamablePickupHandler.onPlayerPickUpStreamablePickup(player, this)
    }

    override fun onDestroy() {
        extensions.destroy()
        destroyPickup()
    }

    private fun updatePickup() {
        if (pickup != null) {
            destroyPickup()
            createPickup()
        }
    }

    private fun destroyPickup() {
        pickup?.apply {
            removeOnPlayerPickUpPickupListener(this@StreamablePickupImpl)
            destroy()
        }
        pickup = null
    }

    private fun createPickup() {
        val pickup = pickupService.createPickup(
                modelId = modelId,
                type = type,
                coordinates = coordinates,
                virtualWorldId = virtualWorldId
        )
        pickup.addOnPlayerPickUpPickupListener(this)
        this.pickup = pickup
    }
}