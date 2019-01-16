package ch.leadrian.samp.kamp.streamer.api.entity

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.TextLabelBase
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableTextLabelStreamInReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableTextLabelStreamOutReceiver
import java.util.Locale

interface StreamableTextLabel :
        TextLabelBase,
        Streamable,
        OnStreamableTextLabelStreamInReceiver,
        OnStreamableTextLabelStreamOutReceiver {

    override var testLOS: Boolean

    val streamDistance: Float

    var interiorIds: MutableSet<Int>

    var virtualWorldIds: MutableSet<Int>

    val isAttached: Boolean

    fun getText(locale: Locale): String

    fun setText(textKey: TextKey)

    fun text(textSupplier: (Locale) -> String)

    fun update(textKey: TextKey, color: Color)

    fun update(color: Color, textSupplier: (Locale) -> String)

    fun isStreamedIn(forPlayer: Player): Boolean

    fun isVisible(forPlayer: Player): Boolean

    fun visibleWhen(condition: StreamableTextLabel.(Player) -> Boolean)

    fun attachTo(player: Player, offset: Vector3D)

    fun attachTo(vehicle: Vehicle, offset: Vector3D)
}