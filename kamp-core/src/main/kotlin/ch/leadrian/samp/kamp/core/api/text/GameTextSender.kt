package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.constants.GameTextStyle
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import javax.inject.Inject

class GameTextSender
@Inject
internal constructor(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val textPreparer: TextPreparer,
        private val playerRegistry: PlayerRegistry
) {

    fun sendGameTextToAll(style: GameTextStyle, time: Int, text: String) {
        nativeFunctionExecutor.gameTextForAll(text = text, time = time, style = style.value)
    }

    fun sendGameTextToAll(style: GameTextStyle, time: Int, text: String, vararg args: Any) {
        textPreparer.prepareForAllPlayers(
                text = text,
                args = args,
                consumer = { player, playerText ->
                    nativeFunctionExecutor.gameTextForPlayer(
                            playerid = player.id.value,
                            text = playerText,
                            time = time,
                            style = style.value
                    )
                },
                consumerForAll = { nativeFunctionExecutor.gameTextForAll(text = it, time = time, style = style.value) }
        )
    }

    fun sendGameTextToAll(style: GameTextStyle, time: Int, textKey: TextKey) {
        textPreparer.prepareForAllPlayers(
                textKey = textKey,
                consumer = { player, playerText ->
                    nativeFunctionExecutor.gameTextForPlayer(
                            playerid = player.id.value,
                            text = playerText,
                            time = time,
                            style = style.value
                    )
                },
                consumerForAll = { nativeFunctionExecutor.gameTextForAll(text = it, time = time, style = style.value) }
        )
    }

    fun sendGameTextToAll(style: GameTextStyle, time: Int, textKey: TextKey, vararg args: Any) {
        textPreparer.prepareForAllPlayers(
                textKey = textKey,
                args = args,
                consumer = { player, playerText ->
                    nativeFunctionExecutor.gameTextForPlayer(
                            playerid = player.id.value,
                            text = playerText,
                            time = time,
                            style = style.value
                    )
                },
                consumerForAll = { nativeFunctionExecutor.gameTextForAll(text = it, time = time, style = style.value) }
        )
    }

    fun sendGameTextToPlayer(player: Player, style: GameTextStyle, time: Int, text: String) {
        nativeFunctionExecutor.gameTextForPlayer(
                playerid = player.id.value,
                text = text,
                time = time,
                style = style.value
        )
    }

    fun sendGameTextToPlayer(player: Player, style: GameTextStyle, time: Int, text: String, vararg args: Any) {
        textPreparer.prepareForPlayer(player, text, args) { _, playerText ->
            nativeFunctionExecutor.gameTextForPlayer(
                    playerid = player.id.value,
                    text = playerText,
                    time = time,
                    style = style.value
            )
        }
    }

    fun sendGameTextToPlayer(player: Player, style: GameTextStyle, time: Int, textKey: TextKey) {
        textPreparer.prepareForPlayer(player, textKey) { _, playerText ->
            nativeFunctionExecutor.gameTextForPlayer(
                    playerid = player.id.value,
                    text = playerText,
                    time = time,
                    style = style.value
            )
        }
    }

    fun sendGameTextToPlayer(player: Player, style: GameTextStyle, time: Int, textKey: TextKey, vararg args: Any) {
        textPreparer.prepareForPlayer(player, textKey, args) { _, playerText ->
            nativeFunctionExecutor.gameTextForPlayer(
                    playerid = player.id.value,
                    text = playerText,
                    time = time,
                    style = style.value
            )
        }
    }

    fun sendGameText(style: GameTextStyle, time: Int, text: String, playerFilter: (Player) -> Boolean) {
        playerRegistry
                .getAll()
                .asSequence()
                .filter(playerFilter)
                .forEach {
                    nativeFunctionExecutor.gameTextForPlayer(
                            playerid = it.id.value,
                            text = text,
                            time = time,
                            style = style.value
                    )
                }
    }

    fun sendGameText(style: GameTextStyle, time: Int, textKey: TextKey, playerFilter: (Player) -> Boolean) {
        textPreparer.prepare(playerFilter, textKey) { player, playerText ->
            nativeFunctionExecutor.gameTextForPlayer(
                    playerid = player.id.value,
                    text = playerText,
                    time = time,
                    style = style.value
            )
        }
    }

    fun sendGameText(
            style: GameTextStyle,
            time: Int,
            text: String,
            vararg args: Any,
            playerFilter: (Player) -> Boolean
    ) {
        textPreparer.prepare(playerFilter, text, args) { player, playerText ->
            nativeFunctionExecutor.gameTextForPlayer(
                    playerid = player.id.value,
                    text = playerText,
                    time = time,
                    style = style.value
            )
        }
    }

    fun sendGameText(
            style: GameTextStyle,
            time: Int,
            textKey: TextKey,
            vararg args: Any,
            playerFilter: (Player) -> Boolean
    ) {
        textPreparer.prepare(playerFilter, textKey, args) { player, playerText ->
            nativeFunctionExecutor.gameTextForPlayer(
                    playerid = player.id.value,
                    text = playerText,
                    time = time,
                    style = style.value
            )
        }
    }

}