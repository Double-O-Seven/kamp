package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDestructionListener
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
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableTextLabelStateFactory
import com.conversantmedia.util.collection.geometry.Rect3d

internal class StreamableTextLabelImpl(
        coordinates: Vector3D,
        text: String,
        color: Color,
        override val streamDistance: Float,
        override val priority: Int,
        override var interiorIds: MutableSet<Int>,
        override var virtualWorldIds: MutableSet<Int>,
        override var testLOS: Boolean,
        private val textProvider: TextProvider,
        private val streamableTextLabelStateFactory: StreamableTextLabelStateFactory
) : DistanceBasedPlayerStreamable,
        SpatiallyIndexedStreamable<StreamableTextLabelImpl, Rect3d>(),
        OnPlayerDisconnectListener,
        OnVehicleDestructionListener,
        StreamableTextLabel {

    private val playerTextLabelsByPlayer: MutableMap<Player, PlayerTextLabel> = mutableMapOf()

    private var textSupplier: (Player) -> String = { text }

    private var state: StreamableTextLabelState = streamableTextLabelStateFactory.createFixedCoordinates(this, coordinates)

    private val onStateChangeHandlers: MutableList<StreamableTextLabelImpl.(StreamableTextLabelState, StreamableTextLabelState) -> Unit> = mutableListOf()

    private var _color: Color = color.toColor()
        set(value) {
            field = value.toColor()
        }

    override var color: Color
        get() = _color
        set(value) {
            _color = value
        }

    override var coordinates: Vector3D
        get() = state.coordinates
        set(value) {
            transitionToState(streamableTextLabelStateFactory.createFixedCoordinates(this, value))
        }

    private fun transitionToState(newState: StreamableTextLabelState) {
        val oldState = this.state
        this.state = newState
        playerTextLabelsByPlayer.replaceAll { player, playerTextLabel ->
            playerTextLabel.destroy()
            newState.createPlayerTextLabel(player)
        }
        onStateChangeHandlers.forEach { it.invoke(this, oldState, newState) }
    }

    internal fun onStateChange(onStateChange: StreamableTextLabelImpl.(StreamableTextLabelState, StreamableTextLabelState) -> Unit) {
        onStateChangeHandlers += onStateChange
    }

    override fun getText(player: Player): String = textSupplier(player)

    override fun setText(textKey: TextKey) {
        setTextSupplier { player -> textProvider.getText(player.locale, textKey) }
    }

    override fun setText(text: String) {
        setTextSupplier { text }
    }

    override fun text(textSupplier: (Player) -> String) {
        setTextSupplier(textSupplier)
    }

    private fun setTextSupplier(textSupplier: (Player) -> String) {
        this.textSupplier = textSupplier
        playerTextLabelsByPlayer.forEach { player, playerTextLabel ->
            playerTextLabel.text = textSupplier(player)
        }
    }

    override fun update(text: String, color: Color) {
        update(color) { text }
    }

    override fun update(textKey: TextKey, color: Color) {
        update(color) { player -> textProvider.getText(player.locale, textKey) }
    }

    override fun update(color: Color, textSupplier: (Player) -> String) {
        _color = color
        this.textSupplier = textSupplier
        playerTextLabelsByPlayer.forEach { player, playerTextLabel ->
            playerTextLabel.update(textSupplier(player), color)
        }
    }

    override val self: StreamableTextLabelImpl = this

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

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) {
            return
        }

        playerTextLabelsByPlayer.values.forEach { it.destroy() }
        playerTextLabelsByPlayer.clear()
        isDestroyed = true
    }

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVehicleDestruction(vehicle: Vehicle) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}