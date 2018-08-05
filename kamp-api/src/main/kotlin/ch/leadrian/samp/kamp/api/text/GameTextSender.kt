package ch.leadrian.samp.kamp.api.text

import ch.leadrian.samp.kamp.api.constants.GameTextStyle
import ch.leadrian.samp.kamp.api.entity.Player

interface GameTextSender {

    fun sendGameTextToAll(style: GameTextStyle, time: Int, text: String)

    fun sendGameTextToAll(style: GameTextStyle, time: Int, text: String, vararg args: Any)

    fun sendGameTextToAll(style: GameTextStyle, time: Int, textKey: TextKey)

    fun sendGameTextToAll(style: GameTextStyle, time: Int, textKey: TextKey, vararg args: Any)

    fun sendGameTextToPlayer(player: Player, style: GameTextStyle, time: Int, text: String)

    fun sendGameTextToPlayer(player: Player, style: GameTextStyle, time: Int, text: String, vararg args: Any)

    fun sendGameTextToPlayer(player: Player, style: GameTextStyle, time: Int, textKey: TextKey)

    fun sendGameTextToPlayer(player: Player, style: GameTextStyle, time: Int, textKey: TextKey, vararg args: Any)

    fun sendGameText(style: GameTextStyle, time: Int, text: String, playerFilter: (Player) -> Boolean)

    fun sendGameText(style: GameTextStyle, time: Int, text: String, vararg args: Any, playerFilter: (Player) -> Boolean)

    fun sendGameText(style: GameTextStyle, time: Int, textKey: TextKey, playerFilter: (Player) -> Boolean)

    fun sendGameText(style: GameTextStyle, time: Int, textKey: TextKey, vararg args: Any, playerFilter: (Player) -> Boolean)

}