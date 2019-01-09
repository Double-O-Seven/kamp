package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextLabel
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableTextLabel
import ch.leadrian.samp.kamp.streamer.runtime.TextLabelStreamer
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableTextLabelStateFactory
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
        private val streamableTextLabelStateFactory: StreamableTextLabelStateFactory
) : CoordinatesBasedPlayerStreamable<StreamableTextLabelImpl, Rect3d>(),
        OnPlayerDisconnectListener,
        StreamableTextLabel {

    private val playerTextLabelsByPlayer: MutableMap<Player, PlayerTextLabel> = mutableMapOf()

    private var state: StreamableTextLabelState = streamableTextLabelStateFactory.createFixedCoordinates(
            this,
            coordinates
    )

    private var _color: Color = color.toColor()
        set(value) {
            field = value.toColor()
        }

    val isAttached: Boolean
        get() = when (state) {
            is StreamableTextLabelState.AttachedToPlayer, is StreamableTextLabelState.AttachedToVehicle -> true
            is StreamableTextLabelState.FixedCoordinates -> false
        }

    override val drawDistance: Float = streamDistance

    override var color: Color
        get() = _color
        set(value) {
            update(value, textSupplier)
        }

    override var coordinates: Vector3D
        get() = state.coordinates
        set(value) {
            transitionToFixedCoordinates(value)
        }

    private fun transitionToState(newState: StreamableTextLabelState) {
        state = newState
        playerTextLabelsByPlayer.replaceAll { player, playerTextLabel ->
            playerTextLabel.destroy()
            newState.createPlayerTextLabel(player)
        }
        textLabelStreamer.onStateChange(this)
    }

    private fun transitionToFixedCoordinates(coordinates: Vector3D) {
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
        _color = color
        this.textSupplier = textSupplier
        playerTextLabelsByPlayer.forEach { player, playerTextLabel ->
            playerTextLabel.update(textSupplier(player.locale), _color)
        }
    }

    override fun distanceTo(location: Location): Float =
            when {
                interiorIds.isNotEmpty() && !interiorIds.contains(location.interiorId) -> Float.MAX_VALUE
                virtualWorldIds.isNotEmpty() && !virtualWorldIds.contains(location.virtualWorldId) -> Float.MAX_VALUE
                else -> coordinates.distanceTo(location)
            }

    override fun onStreamIn(forPlayer: Player) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStreamIn(onStreamIn: StreamableTextLabel.(Player) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStreamOut(forPlayer: Player) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStreamOut(onStreamOut: StreamableTextLabel.(Player) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isStreamedIn(forPlayer: Player): Boolean = playerTextLabelsByPlayer.containsKey(forPlayer)

    override fun getBoundingBox(): Rect3d {
        val coordinates = coordinates
        val minX = coordinates.x - streamDistance
        val minY = coordinates.y - streamDistance
        val minZ = coordinates.z - streamDistance
        val maxX = coordinates.x + streamDistance
        val maxY = coordinates.y + streamDistance
        val maxZ = coordinates.z + streamDistance

        return Rect3d(
                minX.toDouble(),
                minY.toDouble(),
                minZ.toDouble(),
                maxX.toDouble(),
                maxY.toDouble(),
                maxZ.toDouble()
        )
    }

    override fun onDestroy() {
        playerTextLabelsByPlayer.values.forEach { it.destroy() }
        playerTextLabelsByPlayer.clear()
    }

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        val currentState = state
        if (currentState is StreamableTextLabelState.AttachedToPlayer && player == currentState.player) {
            transitionToFixedCoordinates(coordinates)
        }
    }

    private fun onVehicleDestruction(vehicle: Vehicle) {
        val currentState = state
        if (currentState is StreamableTextLabelState.AttachedToVehicle && vehicle == currentState.vehicle) {
            transitionToFixedCoordinates(coordinates)
        }
    }

}