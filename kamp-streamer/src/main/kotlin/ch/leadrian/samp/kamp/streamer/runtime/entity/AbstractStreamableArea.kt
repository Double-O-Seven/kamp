package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerEnterStreamableAreaReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerLeaveStreamableAreaReceiver
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableArea
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableAreaReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableAreaReceiverDelegate
import com.conversantmedia.util.collection.spatial.HyperRect

internal abstract class AbstractStreamableArea<T : HyperRect<*>>(
        final override var interiorIds: MutableSet<Int>,
        final override var virtualWorldIds: MutableSet<Int>,
        final override val priority: Int,
        private val onPlayerEnterStreamableAreaHandler: OnPlayerEnterStreamableAreaHandler,
        private val onPlayerLeaveStreamableAreaHandler: OnPlayerLeaveStreamableAreaHandler,
        private val onPlayerEnterStreamableAreaReceiver: OnPlayerEnterStreamableAreaReceiverDelegate = OnPlayerEnterStreamableAreaReceiverDelegate(),
        private val onPlayerLeaveStreamableAreaReceiver: OnPlayerLeaveStreamableAreaReceiverDelegate = OnPlayerLeaveStreamableAreaReceiverDelegate()
) :
        SpatialIndexBasedStreamable<AbstractStreamableArea<T>, T>(),
        StreamableArea,
        OnPlayerEnterStreamableAreaReceiver by onPlayerEnterStreamableAreaReceiver,
        OnPlayerLeaveStreamableAreaReceiver by onPlayerLeaveStreamableAreaReceiver {

    fun onEnter(player: Player) {
        onPlayerEnterStreamableAreaReceiver.onPlayerEnterStreamableArea(player, this)
        onPlayerEnterStreamableAreaHandler.onPlayerEnterStreamableArea(player, this)
    }

    fun onLeave(player: Player) {
        onPlayerLeaveStreamableAreaReceiver.onPlayerLeaveStreamableArea(player, this)
        onPlayerLeaveStreamableAreaHandler.onPlayerLeaveStreamableArea(player, this)
    }

    abstract operator fun contains(coordinates: Vector3D): Boolean

    operator fun contains(location: Location): Boolean {
        return when {
            interiorIds.isNotEmpty() && location.interiorId !in interiorIds -> false
            virtualWorldIds.isNotEmpty() && location.virtualWorldId !in virtualWorldIds -> false
            else -> contains(location as Vector3D)
        }
    }
}