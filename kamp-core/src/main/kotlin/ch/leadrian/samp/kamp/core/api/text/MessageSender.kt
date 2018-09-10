package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.entity.Player

interface MessageSender {

    fun sendMessageToAll(color: ch.leadrian.samp.kamp.core.api.data.Color, message: String)

    fun sendMessageToAll(color: ch.leadrian.samp.kamp.core.api.data.Color, message: String, vararg args: Any)

    fun sendMessageToAll(color: ch.leadrian.samp.kamp.core.api.data.Color, textKey: TextKey)

    fun sendMessageToAll(color: ch.leadrian.samp.kamp.core.api.data.Color, textKey: TextKey, vararg args: Any)

    fun sendMessageToPlayer(player: Player, color: ch.leadrian.samp.kamp.core.api.data.Color, message: String)

    fun sendMessageToPlayer(player: Player, message: String, vararg args: Any)

    fun sendMessageToPlayer(player: Player, textKey: TextKey)

    fun sendMessageToPlayer(player: Player, textKey: TextKey, vararg args: Any)

    fun sendMessage(color: ch.leadrian.samp.kamp.core.api.data.Color, message: String, playerFilter: (Player) -> Boolean)

    fun sendMessage(color: ch.leadrian.samp.kamp.core.api.data.Color, message: String, vararg args: Any, playerFilter: (Player) -> Boolean)

    fun sendMessage(color: ch.leadrian.samp.kamp.core.api.data.Color, textKey: TextKey, playerFilter: (Player) -> Boolean)

    fun sendMessage(color: ch.leadrian.samp.kamp.core.api.data.Color, textKey: TextKey, vararg args: Any, playerFilter: (Player) -> Boolean)

    fun sendChatMessageToAll(fromPlayer: Player, color: ch.leadrian.samp.kamp.core.api.data.Color, ChatMessage: String)

    fun sendChatMessageToAll(fromPlayer: Player, color: ch.leadrian.samp.kamp.core.api.data.Color, ChatMessage: String, vararg args: Any)

    fun sendChatMessageToAll(fromPlayer: Player, color: ch.leadrian.samp.kamp.core.api.data.Color, textKey: TextKey)

    fun sendChatMessageToAll(fromPlayer: Player, color: ch.leadrian.samp.kamp.core.api.data.Color, textKey: TextKey, vararg args: Any)

    fun sendChatMessageToPlayer(fromPlayer: Player, toPlayer: Player, color: ch.leadrian.samp.kamp.core.api.data.Color, ChatMessage: String)

    fun sendChatMessageToPlayer(fromPlayer: Player, toPlayer: Player, ChatMessage: String, vararg args: Any)

    fun sendChatMessageToPlayer(fromPlayer: Player, toPlayer: Player, textKey: TextKey)

    fun sendChatMessageToPlayer(fromPlayer: Player, toPlayer: Player, textKey: TextKey, vararg args: Any)

    fun sendChatMessage(fromPlayer: Player, color: ch.leadrian.samp.kamp.core.api.data.Color, ChatMessage: String, playerFilter: (Player) -> Boolean)

    fun sendChatMessage(fromPlayer: Player, color: ch.leadrian.samp.kamp.core.api.data.Color, ChatMessage: String, vararg args: Any, playerFilter: (Player) -> Boolean)

    fun sendChatMessage(fromPlayer: Player, color: ch.leadrian.samp.kamp.core.api.data.Color, textKey: TextKey, playerFilter: (Player) -> Boolean)

    fun sendChatMessage(fromPlayer: Player, color: ch.leadrian.samp.kamp.core.api.data.Color, textKey: TextKey, vararg args: Any, playerFilter: (Player) -> Boolean)

}