package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.core.api.constants.MapIconType
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapIcon
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableMapIconStreamInReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableMapIconStreamOutReceiver
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableMapIcon
import ch.leadrian.samp.kamp.streamer.runtime.MapIconStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapIconStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapIconStreamInReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapIconStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapIconStreamOutReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.util.toRect3d
import com.conversantmedia.util.collection.geometry.Rect3d

internal class StreamableMapIconImpl(
        coordinates: Vector3D,
        type: MapIconType,
        color: Color,
        style: MapIconStyle,
        override var virtualWorldIds: MutableSet<Int>,
        override var interiorIds: MutableSet<Int>,
        override val streamDistance: Float,
        override val priority: Int,
        private val mapIconStreamer: MapIconStreamer,
        private val playerMapIconIdAllocator: PlayerMapIconIdAllocator,
        private val onStreamableMapIconStreamInHandler: OnStreamableMapIconStreamInHandler,
        private val onStreamableMapIconStreamOutHandler: OnStreamableMapIconStreamOutHandler,
        private val onStreamableMapIconStreamInReceiver: OnStreamableMapIconStreamInReceiverDelegate = OnStreamableMapIconStreamInReceiverDelegate(),
        private val onStreamableMapIconStreamOutReceiver: OnStreamableMapIconStreamOutReceiverDelegate = OnStreamableMapIconStreamOutReceiverDelegate()
) :
        CoordinatesBasedPlayerStreamable<StreamableMapIconImpl, Rect3d>(),
        StreamableMapIcon,
        OnPlayerDisconnectListener,
        OnStreamableMapIconStreamInReceiver by onStreamableMapIconStreamInReceiver,
        OnStreamableMapIconStreamOutReceiver by onStreamableMapIconStreamOutReceiver {

    private val playerMapIconsByPlayer: MutableMap<Player, PlayerMapIconWrapper> = mutableMapOf()

    override var coordinates: Vector3D = coordinates.toVector3D()
        set(value) {
            field = value.toVector3D()
            playerMapIconsByPlayer.values.forEach { it.playerMapIcon.coordinates = field }
            mapIconStreamer.onBoundingBoxChange(this)
        }

    override var type: MapIconType = type
        set(value) {
            field = value
            playerMapIconsByPlayer.values.forEach { it.playerMapIcon.type = field }
        }

    override var color: Color = color.toColor()
        set(value) {
            field = value.toColor()
            playerMapIconsByPlayer.values.forEach { it.playerMapIcon.color = field }
        }

    override var style: MapIconStyle = style
        set(value) {
            field = value
            playerMapIconsByPlayer.values.forEach { it.playerMapIcon.style = field }
        }

    private var visibilityCondition: StreamableMapIcon.(Player) -> Boolean = { true }

    override fun isVisible(forPlayer: Player): Boolean = visibilityCondition.invoke(this, forPlayer)

    override fun visibleWhen(condition: StreamableMapIcon.(Player) -> Boolean) {
        visibilityCondition = condition
    }

    override fun onStreamIn(forPlayer: Player) {
        requireNotDestroyed()
        if (isStreamedIn(forPlayer)) {
            throw IllegalStateException("Streamable map icon is already streamed in")
        }
        val allocation = playerMapIconIdAllocator.allocate(forPlayer)
        playerMapIconsByPlayer[forPlayer] = PlayerMapIconWrapper(
                playerMapIcon = forPlayer.createMapIcon(allocation.playerMapIconId, coordinates, type, color, style),
                playerMapIconIdAllocation = allocation
        )
        onStreamableMapIconStreamInReceiver.onStreamableMapIconStreamIn(this, forPlayer)
        onStreamableMapIconStreamInHandler.onStreamableMapIconStreamIn(this, forPlayer)
    }

    override fun onStreamOut(forPlayer: Player) {
        requireNotDestroyed()
        if (!isStreamedIn(forPlayer)) {
            throw IllegalStateException("Streamable map icon was not streamed in")
        }
        playerMapIconsByPlayer.remove(forPlayer)?.apply {
            playerMapIcon.destroy()
            playerMapIconIdAllocation.release()
        }
        onStreamableMapIconStreamOutReceiver.onStreamableMapIconStreamOut(this, forPlayer)
        onStreamableMapIconStreamOutHandler.onStreamableMapIconStreamOut(this, forPlayer)
    }

    override fun isStreamedIn(forPlayer: Player): Boolean = playerMapIconsByPlayer.containsKey(forPlayer)

    override fun onDestroy() {
        playerMapIconsByPlayer.values.forEach { destroyPlayerMapIcon(it) }
        playerMapIconsByPlayer.clear()
    }

    private fun destroyPlayerMapIcon(it: PlayerMapIconWrapper) {
        it.playerMapIconIdAllocation.release()
        it.playerMapIcon.destroy()
    }

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        playerMapIconsByPlayer.remove(player)
    }

    override fun getBoundingBox(): Rect3d = coordinates.toRect3d(streamDistance)

    override fun distanceTo(location: Location): Float =
            when {
                interiorIds.isNotEmpty() && !interiorIds.contains(location.interiorId) -> Float.MAX_VALUE
                virtualWorldIds.isNotEmpty() && !virtualWorldIds.contains(location.virtualWorldId) -> Float.MAX_VALUE
                else -> coordinates.distanceTo(location)
            }

    private data class PlayerMapIconWrapper(
            val playerMapIcon: PlayerMapIcon,
            val playerMapIconIdAllocation: PlayerMapIconIdAllocator.Allocation
    )

}