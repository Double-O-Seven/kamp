@file:kotlin.jvm.JvmName("MessageArguments")

package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.entity.Player
import java.util.Locale

object MessageArguments {

    @JvmStatic
    fun coloredNameOf(player: Player): MessageArgument = ColoredPlayerName(player)

    @JvmStatic
    fun translate(translator: (Locale) -> String): MessageArgument = MessageTranslatable(translator)

    @JvmStatic
    fun nameAndIdOf(player: Player): MessageArgument = PlayerNameAndId(player)

    private class ColoredPlayerName(
            private val player: Player
    ) : MessageArgument {

        private val coloredName: String by lazy {
            "${player.color.toEmbeddedString()}${player.name}"
        }

        override fun getText(locale: Locale, color: Color): String = "$coloredName${color.toEmbeddedString()}"
    }

    private class MessageTranslatable(private val translator: (Locale) -> String) : MessageArgument {

        override fun getText(locale: Locale, color: Color): String = translator(locale)

    }

    private class PlayerNameAndId(private val player: Player) : MessageArgument {

        override fun getText(locale: Locale, color: Color): String = "${player.name} (${player.id.value})"

    }

}
