package ch.leadrian.samp.kamp.api.text

import ch.leadrian.samp.kamp.api.constants.GameTextStyle
import ch.leadrian.samp.kamp.api.entity.Player

interface GameTextSender {

    fun sendGameTextToAll(style: GameTextStyle, text: String)

    fun sendGameTextToAll(style: GameTextStyle, text: String, vararg args: Any)

    fun sendGameTextToAll(style: GameTextStyle, textKey: TextKey)

    fun sendGameTextToAll(style: GameTextStyle, textKey: TextKey, vararg args: Any)

    fun sendGameTextToPlayer(player: Player, style: GameTextStyle, text: String)

    fun sendGameTextToPlayer(player: Player, text: String, vararg args: Any)

    fun sendGameTextToPlayer(player: Player, textKey: TextKey)

    fun sendGameTextToPlayer(player: Player, textKey: TextKey, vararg args: Any)

    fun sendGameText(style: GameTextStyle, text: String, playerFilter: (Player) -> Boolean)

    fun sendGameText(style: GameTextStyle, text: String, vararg args: Any, playerFilter: (Player) -> Boolean)

    fun sendGameText(style: GameTextStyle, textKey: TextKey, playerFilter: (Player) -> Boolean)

    fun sendGameText(style: GameTextStyle, textKey: TextKey, vararg args: Any, playerFilter: (Player) -> Boolean)

    fun sanitizePlayerInput(text: String, placeholder: String = "?"): String

}