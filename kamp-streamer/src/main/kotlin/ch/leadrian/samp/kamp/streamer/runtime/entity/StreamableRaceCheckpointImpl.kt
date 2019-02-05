package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterRaceCheckpointListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerLeaveRaceCheckpointListener
import ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.core.api.service.RaceCheckpointService
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerEnterStreamableRaceCheckpointReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerLeaveStreamableRaceCheckpointReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableRaceCheckpointStreamInReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableRaceCheckpointStreamOutReceiver
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableRaceCheckpoint
import ch.leadrian.samp.kamp.streamer.runtime.RaceCheckpointStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableRaceCheckpointHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableRaceCheckpointReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableRaceCheckpointHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableRaceCheckpointReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableRaceCheckpointStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableRaceCheckpointStreamInReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableRaceCheckpointStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableRaceCheckpointStreamOutReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.util.toRect3d
import com.conversantmedia.util.collection.geometry.Rect3d

internal class StreamableRaceCheckpointImpl(
        coordinates: Vector3D,
        nextCoordinates: Vector3D?,
        size: Float,
        type: RaceCheckpointType,
        override var interiorIds: MutableSet<Int>,
        override var virtualWorldIds: MutableSet<Int>,
        override val priority: Int,
        override val streamDistance: Float,
        private val raceCheckpointStreamer: RaceCheckpointStreamer,
        raceCheckpointService: RaceCheckpointService,
        private val onStreamableRaceCheckpointStreamInHandler: OnStreamableRaceCheckpointStreamInHandler,
        private val onStreamableRaceCheckpointStreamOutHandler: OnStreamableRaceCheckpointStreamOutHandler,
        private val onPlayerEnterStreamableRaceCheckpointHandler: OnPlayerEnterStreamableRaceCheckpointHandler,
        private val onPlayerLeaveStreamableRaceCheckpointHandler: OnPlayerLeaveStreamableRaceCheckpointHandler,
        private val onStreamableRaceCheckpointStreamInReceiver: OnStreamableRaceCheckpointStreamInReceiverDelegate = OnStreamableRaceCheckpointStreamInReceiverDelegate(),
        private val onStreamableRaceCheckpointStreamOutReceiver: OnStreamableRaceCheckpointStreamOutReceiverDelegate = OnStreamableRaceCheckpointStreamOutReceiverDelegate(),
        private val onPlayerEnterStreamableRaceCheckpointReceiver: OnPlayerEnterStreamableRaceCheckpointReceiverDelegate = OnPlayerEnterStreamableRaceCheckpointReceiverDelegate(),
        private val onPlayerLeaveStreamableRaceCheckpointReceiver: OnPlayerLeaveStreamableRaceCheckpointReceiverDelegate = OnPlayerLeaveStreamableRaceCheckpointReceiverDelegate()
) :
        CoordinatesBasedPlayerStreamable<StreamableRaceCheckpointImpl, Rect3d>(),
        StreamableRaceCheckpoint,
        OnStreamableRaceCheckpointStreamInReceiver by onStreamableRaceCheckpointStreamInReceiver,
        OnStreamableRaceCheckpointStreamOutReceiver by onStreamableRaceCheckpointStreamOutReceiver,
        OnPlayerEnterStreamableRaceCheckpointReceiver by onPlayerEnterStreamableRaceCheckpointReceiver,
        OnPlayerLeaveStreamableRaceCheckpointReceiver by onPlayerLeaveStreamableRaceCheckpointReceiver,
        OnPlayerEnterRaceCheckpointListener,
        OnPlayerLeaveRaceCheckpointListener {

    private val raceCheckpoint = raceCheckpointService.createRaceCheckpoint(
            coordinates = coordinates,
            size = size,
            type = type,
            nextCoordinates = nextCoordinates
    ).also {
        it.addOnPlayerEnterRaceCheckpointListener(this)
        it.addOnPlayerLeaveRaceCheckpointListener(this)
    }

    override var coordinates: Vector3D
        get() = raceCheckpoint.coordinates
        set(value) {
            raceCheckpoint.coordinates = value
            raceCheckpointStreamer.onBoundingBoxChange(this)
        }

    override var size: Float
        get() = raceCheckpoint.size
        set(value) {
            raceCheckpoint.size = value
        }

    override var type: RaceCheckpointType
        get() = raceCheckpoint.type
        set(value) {
            raceCheckpoint.type = value
        }

    override var nextCoordinates: Vector3D?
        get() = raceCheckpoint.nextCoordinates
        set(value) {
            raceCheckpoint.nextCoordinates = value
        }

    private var visibilityCondition: StreamableRaceCheckpoint.(Player) -> Boolean = { true }

    override fun isVisible(forPlayer: Player): Boolean = visibilityCondition.invoke(this, forPlayer)

    override fun visibleWhen(condition: StreamableRaceCheckpoint.(Player) -> Boolean) {
        visibilityCondition = condition
    }

    override fun contains(player: Player): Boolean = player in raceCheckpoint

    override fun isStreamedIn(forPlayer: Player): Boolean = forPlayer.raceCheckpoint == raceCheckpoint

    override fun onStreamIn(forPlayer: Player) {
        requireNotDestroyed()
        if (isStreamedIn(forPlayer)) {
            throw IllegalStateException("Streamable raceCheckpoint is already streamed in")
        }
        forPlayer.raceCheckpoint = raceCheckpoint
        onStreamableRaceCheckpointStreamInReceiver.onStreamableRaceCheckpointStreamIn(this, forPlayer)
        onStreamableRaceCheckpointStreamInHandler.onStreamableRaceCheckpointStreamIn(this, forPlayer)
    }

    override fun onStreamOut(forPlayer: Player) {
        requireNotDestroyed()
        if (!isStreamedIn(forPlayer)) {
            return
        }
        forPlayer.raceCheckpoint = null
        onStreamableRaceCheckpointStreamOutReceiver.onStreamableRaceCheckpointStreamOut(this, forPlayer)
        onStreamableRaceCheckpointStreamOutHandler.onStreamableRaceCheckpointStreamOut(this, forPlayer)
    }

    override fun onPlayerEnterRaceCheckpoint(player: Player) {
        onPlayerEnterStreamableRaceCheckpointReceiver.onPlayerEnterStreamableRaceCheckpoint(player, this)
        onPlayerEnterStreamableRaceCheckpointHandler.onPlayerEnterStreamableRaceCheckpoint(player, this)
    }

    override fun onPlayerLeaveRaceCheckpoint(player: Player) {
        onPlayerLeaveStreamableRaceCheckpointReceiver.onPlayerLeaveStreamableRaceCheckpoint(player, this)
        onPlayerLeaveStreamableRaceCheckpointHandler.onPlayerLeaveStreamableRaceCheckpoint(player, this)
    }

    override fun onDestroy() {
        raceCheckpoint.destroy()
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