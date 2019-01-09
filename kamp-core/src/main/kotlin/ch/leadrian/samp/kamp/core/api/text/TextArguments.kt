package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.entity.Player
import java.util.Locale

object TextArguments {

    @JvmStatic
    fun translate(translator: (Locale) -> String): TextArgument = TextTranslatable(translator)

    @JvmStatic
    fun nameAndIdOf(player: Player): TextArgument = PlayerNameAndId(player)

    private class TextTranslatable(private val translator: (Locale) -> String) : TextArgument {

        override fun getText(locale: Locale): String = translator(locale)

    }

    private class PlayerNameAndId(private val player: Player) : TextArgument {

        override fun getText(locale: Locale): String = "${player.name} (${player.id.value})"

    }

}
