@file:kotlin.jvm.JvmName("MessageArguments")

package ch.leadrian.samp.kamp.api.text

import ch.leadrian.samp.kamp.api.data.Color
import ch.leadrian.samp.kamp.api.entity.Player
import java.util.*

private class EmbeddedPlayerName constructor(
        private val player: Player
) : MessageArgument {

    private val coloredName: String by lazy {
        "${player.color.toEmbeddedString()}${player.name}"
    }

    override fun get(locale: Locale, color: Color): String = "$coloredName${color.toEmbeddedString()}"
}

fun embeddedPlayerNameOf(player: Player): MessageArgument = EmbeddedPlayerName(player)