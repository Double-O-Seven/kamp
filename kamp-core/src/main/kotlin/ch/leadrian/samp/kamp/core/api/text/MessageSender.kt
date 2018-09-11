package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import javax.inject.Inject

class MessageSender
@Inject
internal constructor(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val playerRegistry: PlayerRegistry,
        private val messagePreparer: MessagePreparer
) {

    fun sendMessageToAll(color: Color, message: String) {
        nativeFunctionExecutor.sendClientMessageToAll(color.value, message)
    }

    fun sendMessageToAll(color: Color, message: String, vararg args: Any) {
        messagePreparer.prepareForAllPlayers(
                color = color,
                text = message,
                args = args,
                consumer = { player, playerMessage ->
                    nativeFunctionExecutor.sendClientMessage(
                            playerid = player.id.value,
                            message = playerMessage,
                            color = color.value
                    )
                },
                consumerForAll = { nativeFunctionExecutor.sendClientMessageToAll(color.value, it) }
        )
    }

    fun sendMessageToAll(color: Color, textKey: TextKey) {
        messagePreparer.prepareForAllPlayers(
                textKey = textKey,
                consumer = { player, playerMessage ->
                    nativeFunctionExecutor.sendClientMessage(
                            playerid = player.id.value,
                            message = playerMessage,
                            color = color.value
                    )
                },
                consumerForAll = { nativeFunctionExecutor.sendClientMessageToAll(color.value, it) }
        )
    }

    fun sendMessageToAll(color: Color, textKey: TextKey, vararg args: Any) {
        messagePreparer.prepareForAllPlayers(
                color = color,
                textKey = textKey,
                args = args,
                consumer = { player, playerMessage ->
                    nativeFunctionExecutor.sendClientMessage(
                            playerid = player.id.value,
                            message = playerMessage,
                            color = color.value
                    )
                },
                consumerForAll = { nativeFunctionExecutor.sendClientMessageToAll(color.value, it) }
        )
    }

    fun sendMessageToPlayer(player: Player, color: Color, message: String) {
        nativeFunctionExecutor.sendClientMessage(playerid = player.id.value, color = color.value, message = message)
    }

    fun sendMessageToPlayer(player: Player, color: Color, message: String, vararg args: Any) {
        messagePreparer.prepareForPlayer(color, player, message, args) { _, playerMessage ->
            nativeFunctionExecutor.sendClientMessage(
                    playerid = player.id.value,
                    color = color.value,
                    message = playerMessage
            )
        }
    }

    fun sendMessageToPlayer(player: Player, color: Color, textKey: TextKey) {
        messagePreparer.prepareForPlayer(player, textKey) { _, playerMessage ->
            nativeFunctionExecutor.sendClientMessage(
                    playerid = player.id.value,
                    color = color.value,
                    message = playerMessage
            )
        }
    }

    fun sendMessageToPlayer(player: Player, color: Color, textKey: TextKey, vararg args: Any) {
        messagePreparer.prepareForPlayer(color, player, textKey, args) { _, playerMessage ->
            nativeFunctionExecutor.sendClientMessage(
                    playerid = player.id.value,
                    color = color.value,
                    message = playerMessage
            )
        }
    }

    fun sendMessage(color: Color, message: String, playerFilter: (Player) -> Boolean) {
        playerRegistry
                .getAll()
                .asSequence()
                .filter { playerFilter(it) }
                .forEach {
                    nativeFunctionExecutor.sendClientMessage(
                            playerid = it.id.value,
                            color = color.value,
                            message = message
                    )
                }
    }

    fun sendMessage(color: Color, message: String, vararg args: Any, playerFilter: (Player) -> Boolean) {
        messagePreparer.prepare(color, playerFilter, message, args) { player, playerMessage ->
            nativeFunctionExecutor.sendClientMessage(
                    playerid = player.id.value,
                    color = color.value,
                    message = playerMessage
            )
        }
    }

    fun sendMessage(color: Color, textKey: TextKey, playerFilter: (Player) -> Boolean) {
        messagePreparer.prepare(playerFilter, textKey) { player, playerMessage ->
            nativeFunctionExecutor.sendClientMessage(
                    playerid = player.id.value,
                    color = color.value,
                    message = playerMessage
            )
        }
    }

    fun sendMessage(color: Color, textKey: TextKey, vararg args: Any, playerFilter: (Player) -> Boolean) {
        messagePreparer.prepare(color, playerFilter, textKey, args) { player, playerMessage ->
            nativeFunctionExecutor.sendClientMessage(
                    playerid = player.id.value,
                    color = color.value,
                    message = playerMessage
            )
        }
    }

}