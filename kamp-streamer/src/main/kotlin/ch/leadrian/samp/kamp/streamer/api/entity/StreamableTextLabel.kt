package ch.leadrian.samp.kamp.streamer.api.entity

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Destroyable
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey

interface StreamableTextLabel : Destroyable {

    val streamDistance: Float

    var interiorIds: MutableSet<Int>

    var virtualWorldIds: MutableSet<Int>

    var testLOS: Boolean

    var color: Color

    var coordinates: Vector3D

    fun getText(player: Player): String

    fun setText(textKey: TextKey)

    fun setText(text: String)

    fun text(textSupplier: (Player) -> String)

    fun update(text: String, color: Color)

    fun update(textKey: TextKey, color: Color)

    fun update(color: Color, textSupplier: (Player) -> String)

    fun onStreamIn(onStreamIn: StreamableTextLabel.(Player) -> Unit)

    fun onStreamOut(onStreamOut: StreamableTextLabel.(Player) -> Unit)

    fun isStreamedIn(forPlayer: Player): Boolean
}