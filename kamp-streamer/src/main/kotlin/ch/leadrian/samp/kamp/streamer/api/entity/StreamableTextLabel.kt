package ch.leadrian.samp.kamp.streamer.api.entity

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.TextLabelBase
import ch.leadrian.samp.kamp.core.api.text.TextKey
import java.util.*

interface StreamableTextLabel : TextLabelBase {

    val streamDistance: Float

    var interiorIds: MutableSet<Int>

    var virtualWorldIds: MutableSet<Int>

    fun getText(locale: Locale): String

    fun setText(textKey: TextKey)

    fun text(textSupplier: (Locale) -> String)

    fun update(textKey: TextKey, color: Color)

    fun update(color: Color, textSupplier: (Locale) -> String)

    fun onStreamIn(onStreamIn: StreamableTextLabel.(Player) -> Unit)

    fun onStreamOut(onStreamOut: StreamableTextLabel.(Player) -> Unit)

    fun isStreamedIn(forPlayer: Player): Boolean
}