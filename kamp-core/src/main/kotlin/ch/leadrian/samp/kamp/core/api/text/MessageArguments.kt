@file:kotlin.jvm.JvmName("MessageArguments")

package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.entity.Player
import java.util.*

private class ColoredPlayerName(
        private val player: Player
) : MessageArgument {

    private val coloredName: String by lazy {
        "${player.color.toEmbeddedString()}${player.name}"
    }

    override fun getText(locale: Locale, color: Color): String = "$coloredName${color.toEmbeddedString()}"
}

fun coloredNameOf(player: Player): MessageArgument = ColoredPlayerName(player)

private class MessageTranslatable(private val translator: (Locale) -> String) : MessageArgument {

    override fun getText(locale: Locale, color: Color): String = translator(locale)

}

fun translateForMessage(translator: (Locale) -> String): MessageArgument = MessageTranslatable(translator)
