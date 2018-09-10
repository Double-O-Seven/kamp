package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.entity.Player

interface GameTextSender {

    fun sendGameTextToAll(style: ch.leadrian.samp.kamp.core.api.constants.GameTextStyle, time: Int, text: String)

    fun sendGameTextToAll(style: ch.leadrian.samp.kamp.core.api.constants.GameTextStyle, time: Int, text: String, vararg args: Any)

    fun sendGameTextToAll(style: ch.leadrian.samp.kamp.core.api.constants.GameTextStyle, time: Int, textKey: TextKey)

    fun sendGameTextToAll(style: ch.leadrian.samp.kamp.core.api.constants.GameTextStyle, time: Int, textKey: TextKey, vararg args: Any)

    fun sendGameTextToPlayer(player: Player, style: ch.leadrian.samp.kamp.core.api.constants.GameTextStyle, time: Int, text: String)

    fun sendGameTextToPlayer(player: Player, style: ch.leadrian.samp.kamp.core.api.constants.GameTextStyle, time: Int, text: String, vararg args: Any)

    fun sendGameTextToPlayer(player: Player, style: ch.leadrian.samp.kamp.core.api.constants.GameTextStyle, time: Int, textKey: TextKey)

    fun sendGameTextToPlayer(player: Player, style: ch.leadrian.samp.kamp.core.api.constants.GameTextStyle, time: Int, textKey: TextKey, vararg args: Any)

    fun sendGameText(style: ch.leadrian.samp.kamp.core.api.constants.GameTextStyle, time: Int, text: String, playerFilter: (Player) -> Boolean)

    fun sendGameText(style: ch.leadrian.samp.kamp.core.api.constants.GameTextStyle, time: Int, text: String, vararg args: Any, playerFilter: (Player) -> Boolean)

    fun sendGameText(style: ch.leadrian.samp.kamp.core.api.constants.GameTextStyle, time: Int, textKey: TextKey, playerFilter: (Player) -> Boolean)

    fun sendGameText(style: ch.leadrian.samp.kamp.core.api.constants.GameTextStyle, time: Int, textKey: TextKey, vararg args: Any, playerFilter: (Player) -> Boolean)

}