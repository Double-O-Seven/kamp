package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Destroyable
import ch.leadrian.samp.kamp.core.api.entity.OnDestroyListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextLabel
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableTextLabelStreamInReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableTextLabelStreamOutReceiver
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableTextLabel
import ch.leadrian.samp.kamp.streamer.runtime.TextLabelStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableTextLabelStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableTextLabelStreamInReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableTextLabelStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableTextLabelStreamOutReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableTextLabelStateFactory
import ch.leadrian.samp.kamp.streamer.runtime.util.toRect3d
import com.conversantmedia.util.collection.geometry.Rect3d
import java.util.Locale

internal class StreamableTextLabelImpl(
        coordinates: Vector3D,
        color: Color,
        private var textSupplier: (Locale) -> String,
        override val streamDistance: Float,
        override val priority: Int,
        override var interiorIds: MutableSet<Int>,
        override var virtualWorldIds: MutableSet<Int>,
        override var testLOS: Boolean,
        private val textProvider: TextProvider,
        private val textLabelStreamer: TextLabelStreamer,
        private val streamableTextLabelStateFactory: StreamableTextLabelStateFactory,
        private val onStreamableTextLabelStreamInHandler: OnStreamableTextLabelStreamInHandler,
        private val onStreamableTextLabelStreamOutHandler: OnStreamableTextLabelStreamOutHandler,
        private val onStreamableTextLabelStreamInReceiver: OnStreamableTextLabelStreamInReceiverDelegate = OnStreamableTextLabelStreamInReceiverDelegate(),
        private val onStreamableTextLabelStreamOutReceiver: OnStreamableTextLabelStreamOutReceiverDelegate = OnStreamableTextLabelStreamOutReceiverDelegate()
) :
        CoordinatesBasedPlayerStreamable<StreamableTextLabelImpl, Rect3d>(),
        StreamableTextLabel,
        OnPlayerDisconnectListener,
        OnDestroyListener,
        OnStreamableTextLabelStreamInReceiver by onStreamableTextLabelStreamInReceiver,
        OnStreamableTextLabelStreamOutReceiver by onStreamableTextLabelStreamOutReceiver {

    private val playerTextLabelsByPlayer: MutableMap<Player, PlayerTextLabel> = mutableMapOf()

    private var currentState: StreamableTextLabelState = streamableTextLabelStateFactory.createFixedCoordinates(
            this,
            coordinates
    )

    override val isAttached: Boolean
        get() = when (currentState) {
            is StreamableTextLabelState.AttachedToPlayer, is StreamableTextLabelState.AttachedToVehicle -> true
            is StreamableTextLabelState.FixedCoordinates -> false
        }

    override val drawDistance: Float = streamDistance

    private var _color: Color = color.toColor()
        set(value) {
            field = value.toColor()
        }

    override var color: Color
        get() = _color
        set(value) {
            update(value, textSupplier)
        }

    private var visibilityCondition: StreamableTextLabel.(Player) -> Boolean = { true }

    override fun isVisible(forPlayer: Player): Boolean = visibilityCondition.invoke(this, forPlayer)

    override fun visibleWhen(condition: StreamableTextLabel.(Player) -> Boolean) {
        visibilityCondition = condition
    }

    override var coordinates: Vector3D
        get() = currentState.coordinates
        set(value) {
            requireNotDestroyed()
            fixCoordinates(value)
            textLabelStreamer.onBoundingBoxChange(this)
        }

    private fun fixCoordinates(coordinates: Vector3D) {
        transitionToState(streamableTextLabelStateFactory.createFixedCoordinates(this, coordinates))
        textLabelStreamer.onBoundingBoxChange(this)
    }

    override var text: String
        get() = getText(Locale.getDefault())
        set(value) {
            text { value }
        }

    override fun getText(locale: Locale): String = textSupplier(locale)

    override fun setText(textKey: TextKey) {
        text { locale -> textProvider.getText(locale, textKey) }
    }

    override fun text(textSupplier: (Locale) -> String) {
        update(_color, textSupplier)
    }

    override fun update(text: String, color: Color) {
        update(color) { text }
    }

    override fun update(textKey: TextKey, color: Color) {
        update(color) { locale -> textProvider.getText(locale, textKey) }
    }

    override fun update(color: Color, textSupplier: (Locale) -> String) {
        requireNotDestroyed()
        _color = color
        this.textSupplier = textSupplier
        playerTextLabelsByPlayer.forEach { player, playerTextLabel ->
            playerTextLabel.update(textSupplier(player.locale), _color)
        }
    }

    override fun attachTo(player: Player, offset: Vector3D) {
        requireNotDestroyed()
        transitionToState(streamableTextLabelStateFactory.createAttachedToPlayer(this, offset, player))
    }

    override fun attachTo(vehicle: Vehicle, offset: Vector3D) {
        requireNotDestroyed()
        transitionToState(streamableTextLabelStateFactory.createAttachedToVehicle(this, offset, vehicle))
        vehicle.addOnDestroyListener(this)
    }

    private fun transitionToState(newState: StreamableTextLabelState) {
        removeOnDestroyListener()
        currentState = newState
        playerTextLabelsByPlayer.replaceAll { player, playerTextLabel ->
            playerTextLabel.destroy()
            newState.createPlayerTextLabel(player)
        }
        textLabelStreamer.onStateChange(this)
    }

    override fun distanceTo(location: Location): Float =
            when {
                interiorIds.isNotEmpty() && !interiorIds.contains(location.interiorId) -> Float.MAX_VALUE
                virtualWorldIds.isNotEmpty() && !virtualWorldIds.contains(location.virtualWorldId) -> Float.MAX_VALUE
                else -> coordinates.distanceTo(location)
            }

    override fun onStreamIn(forPlayer: Player) {
        requireNotDestroyed()
        if (isStreamedIn(forPlayer)) {
            throw IllegalStateException("Streamable text label is already streamed in")
        }
        playerTextLabelsByPlayer[forPlayer] = currentState.createPlayerTextLabel(forPlayer)
        onStreamableTextLabelStreamInReceiver.onStreamableTextLabelStreamIn(this, forPlayer)
        onStreamableTextLabelStreamInHandler.onStreamableTextLabelStreamIn(this, forPlayer)
    }

    override fun onStreamOut(forPlayer: Player) {
        requireNotDestroyed()
        val playerTextLabel = playerTextLabelsByPlayer.remove(forPlayer)
                ?: throw IllegalStateException("Streamable text label was not streamed in")
        playerTextLabel.destroy()
        onStreamableTextLabelStreamOutReceiver.onStreamableTextLabelStreamOut(this, forPlayer)
        onStreamableTextLabelStreamOutHandler.onStreamableTextLabelStreamOut(this, forPlayer)
    }

    override fun isStreamedIn(forPlayer: Player): Boolean = playerTextLabelsByPlayer.containsKey(forPlayer)

    override val boundingBox: Rect3d
        get() = coordinates.toRect3d(streamDistance)

    override fun onDestroy() {
        removeOnDestroyListener()
        playerTextLabelsByPlayer.values.forEach { it.destroy() }
        playerTextLabelsByPlayer.clear()
    }

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        val currentState = currentState
        if (currentState is StreamableTextLabelState.AttachedToPlayer && player == currentState.player) {
            fixCoordinates(coordinates)
        }
    }

    override fun onDestroy(destroyable: Destroyable) {
        val currentState1 = currentState
        if (currentState1 is StreamableTextLabelState.AttachedToVehicle && currentState1.vehicle == destroyable) {
            fixCoordinates(coordinates)
        }
    }

    private fun removeOnDestroyListener() {
        val currentState = currentState
        if (currentState is StreamableTextLabelState.AttachedToVehicle) {
            currentState.vehicle.removeOnDestroyListener(this)
        }
    }

}