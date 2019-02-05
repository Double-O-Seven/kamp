package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterCheckpointListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerLeaveCheckpointListener
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.core.api.service.CheckpointService
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerEnterStreamableCheckpointReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerLeaveStreamableCheckpointReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableCheckpointStreamInReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableCheckpointStreamOutReceiver
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableCheckpoint
import ch.leadrian.samp.kamp.streamer.runtime.CheckpointStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableCheckpointHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableCheckpointReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableCheckpointHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableCheckpointReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableCheckpointStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableCheckpointStreamInReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableCheckpointStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableCheckpointStreamOutReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.util.toRect3d
import com.conversantmedia.util.collection.geometry.Rect3d

internal class StreamableCheckpointImpl(
        coordinates: Vector3D,
        size: Float,
        override var interiorIds: MutableSet<Int>,
        override var virtualWorldIds: MutableSet<Int>,
        override val priority: Int,
        override val streamDistance: Float,
        private val checkpointStreamer: CheckpointStreamer,
        checkpointService: CheckpointService,
        private val onStreamableCheckpointStreamInHandler: OnStreamableCheckpointStreamInHandler,
        private val onStreamableCheckpointStreamOutHandler: OnStreamableCheckpointStreamOutHandler,
        private val onPlayerEnterStreamableCheckpointHandler: OnPlayerEnterStreamableCheckpointHandler,
        private val onPlayerLeaveStreamableCheckpointHandler: OnPlayerLeaveStreamableCheckpointHandler,
        private val onStreamableCheckpointStreamInReceiver: OnStreamableCheckpointStreamInReceiverDelegate = OnStreamableCheckpointStreamInReceiverDelegate(),
        private val onStreamableCheckpointStreamOutReceiver: OnStreamableCheckpointStreamOutReceiverDelegate = OnStreamableCheckpointStreamOutReceiverDelegate(),
        private val onPlayerEnterStreamableCheckpointReceiver: OnPlayerEnterStreamableCheckpointReceiverDelegate = OnPlayerEnterStreamableCheckpointReceiverDelegate(),
        private val onPlayerLeaveStreamableCheckpointReceiver: OnPlayerLeaveStreamableCheckpointReceiverDelegate = OnPlayerLeaveStreamableCheckpointReceiverDelegate()
) :
        CoordinatesBasedPlayerStreamable<StreamableCheckpointImpl, Rect3d>(),
        StreamableCheckpoint,
        OnStreamableCheckpointStreamInReceiver by onStreamableCheckpointStreamInReceiver,
        OnStreamableCheckpointStreamOutReceiver by onStreamableCheckpointStreamOutReceiver,
        OnPlayerEnterStreamableCheckpointReceiver by onPlayerEnterStreamableCheckpointReceiver,
        OnPlayerLeaveStreamableCheckpointReceiver by onPlayerLeaveStreamableCheckpointReceiver,
        OnPlayerEnterCheckpointListener,
        OnPlayerLeaveCheckpointListener {

    private val checkpoint = checkpointService.createCheckpoint(coordinates, size).also {
        it.addOnPlayerEnterCheckpointListener(this)
        it.addOnPlayerLeaveCheckpointListener(this)
    }

    override var coordinates: Vector3D
        get() = checkpoint.coordinates
        set(value) {
            checkpoint.coordinates = value.toVector3D()
            checkpointStreamer.onBoundingBoxChange(this)
        }

    override var size: Float
        get() = checkpoint.size
        set(value) {
            checkpoint.size = value
        }

    private var visibilityCondition: StreamableCheckpoint.(Player) -> Boolean = { true }

    override fun isVisible(forPlayer: Player): Boolean = visibilityCondition.invoke(this, forPlayer)

    override fun visibleWhen(condition: StreamableCheckpoint.(Player) -> Boolean) {
        visibilityCondition = condition
    }

    override fun contains(player: Player): Boolean = player in checkpoint

    override fun isStreamedIn(forPlayer: Player): Boolean = forPlayer.checkpoint == checkpoint

    override fun onStreamIn(forPlayer: Player) {
        requireNotDestroyed()
        if (isStreamedIn(forPlayer)) {
            throw IllegalStateException("Streamable checkpoint is already streamed in")
        }
        forPlayer.checkpoint = checkpoint
        onStreamableCheckpointStreamInReceiver.onStreamableCheckpointStreamIn(this, forPlayer)
        onStreamableCheckpointStreamInHandler.onStreamableCheckpointStreamIn(this, forPlayer)
    }

    override fun onStreamOut(forPlayer: Player) {
        requireNotDestroyed()
        if (!isStreamedIn(forPlayer)) {
            return
        }
        forPlayer.checkpoint = null
        onStreamableCheckpointStreamOutReceiver.onStreamableCheckpointStreamOut(this, forPlayer)
        onStreamableCheckpointStreamOutHandler.onStreamableCheckpointStreamOut(this, forPlayer)
    }

    override fun onPlayerEnterCheckpoint(player: Player) {
        onPlayerEnterStreamableCheckpointReceiver.onPlayerEnterStreamableCheckpoint(player, this)
        onPlayerEnterStreamableCheckpointHandler.onPlayerEnterStreamableCheckpoint(player, this)
    }

    override fun onPlayerLeaveCheckpoint(player: Player) {
        onPlayerLeaveStreamableCheckpointReceiver.onPlayerLeaveStreamableCheckpoint(player, this)
        onPlayerLeaveStreamableCheckpointHandler.onPlayerLeaveStreamableCheckpoint(player, this)
    }

    override fun onDestroy() {
        checkpoint.destroy()
    }

    override val boundingBox: Rect3d
        get() = coordinates.toRect3d(streamDistance)

    override fun distanceTo(location: Location): Float =
            when {
                interiorIds.isNotEmpty() && !interiorIds.contains(location.interiorId) -> Float.MAX_VALUE
                virtualWorldIds.isNotEmpty() && !virtualWorldIds.contains(location.virtualWorldId) -> Float.MAX_VALUE
                else -> coordinates.distanceTo(location)
            }

}