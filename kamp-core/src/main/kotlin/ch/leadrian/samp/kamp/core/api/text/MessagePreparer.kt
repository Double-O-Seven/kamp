package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import java.util.*
import javax.inject.Inject

class MessagePreparer
@Inject
internal constructor(
        private val playerRegistry: PlayerRegistry,
        private val textProvider: TextProvider,
        private val messageFormatter: MessageFormatter
) {

    fun prepareForAllPlayers(
            color: Color,
            text: String,
            args: Array<out Any>,
            consumer: (Player, String) -> Unit,
            consumerForAll: (String) -> Unit
    ) {
        val players = playerRegistry.getAll()
        val formattedMessages = getFormattedMessages(color, players, text, args)
        prepare(players, formattedMessages, consumer, consumerForAll)
    }

    fun prepareForAllPlayers(
            textKey: TextKey,
            consumer: (Player, String) -> Unit,
            consumerForAll: (String) -> Unit
    ) {
        val players = playerRegistry.getAll()
        val translatedMessages = getTranslatedMessage(players, textKey)
        prepare(players, translatedMessages, consumer, consumerForAll)
    }

    fun prepareForAllPlayers(
            color: Color,
            textKey: TextKey,
            args: Array<out Any>,
            consumer: (Player, String) -> Unit,
            consumerForAll: (String) -> Unit
    ) {
        val players = playerRegistry.getAll()
        val formattedTranslatedMessages = getFormattedTranslatedMessages(color, players, textKey, args)
        prepare(players, formattedTranslatedMessages, consumer, consumerForAll)
    }

    fun prepareForPlayer(
            color: Color,
            player: Player,
            text: String,
            args: Array<out Any>,
            consumer: (Player, String) -> Unit
    ) {
        val formattedMessage = messageFormatter.format(player.locale, color, text, *args)
        consumer(player, formattedMessage)
    }

    fun prepareForPlayer(
            player: Player,
            textKey: TextKey,
            consumer: (Player, String) -> Unit
    ) {
        val text = textProvider.getText(player.locale, textKey)
        consumer(player, text)
    }

    fun prepareForPlayer(
            color: Color,
            player: Player,
            textKey: TextKey,
            args: Array<out Any>,
            consumer: (Player, String) -> Unit
    ) {
        val text = textProvider.getText(player.locale, textKey)
        prepareForPlayer(color, player, text, args, consumer)
    }

    fun prepare(
            color: Color,
            playerFilter: (Player) -> Boolean,
            text: String,
            args: Array<out Any>,
            consumer: (Player, String) -> Unit
    ) {
        val players = playerRegistry.getAll().filter(playerFilter)
        val formattedMessages = getFormattedMessages(color, players, text, args)
        prepare(players, formattedMessages, consumer)
    }

    fun prepare(
            playerFilter: (Player) -> Boolean,
            textKey: TextKey,
            consumer: (Player, String) -> Unit
    ) {
        val players = playerRegistry.getAll().filter(playerFilter)
        val translatedMessages = getTranslatedMessage(players, textKey)
        prepare(players, translatedMessages, consumer)
    }

    fun prepare(
            color: Color,
            playerFilter: (Player) -> Boolean,
            textKey: TextKey,
            args: Array<out Any>,
            consumer: (Player, String) -> Unit
    ) {
        val players = playerRegistry.getAll().filter(playerFilter)
        val formattedTranslatedMessages = getFormattedTranslatedMessages(color, players, textKey, args)
        prepare(players, formattedTranslatedMessages, consumer)
    }

    private fun prepare(
            players: List<Player>,
            translatedMessages: Map<Locale, String>,
            consumer: (Player, String) -> Unit,
            consumerForAll: ((String) -> Unit)? = null
    ) {
        if (consumerForAll != null && translatedMessages.size == 1) {
            consumerForAll(translatedMessages.values.first())
        } else {
            players.forEach { consumer(it, translatedMessages[it.locale]!!) }
        }
    }

    private fun getTranslatedMessage(players: List<Player>, textKey: TextKey): Map<Locale, String> =
            players
                    .asSequence()
                    .map { it.locale }
                    .distinct()
                    .associateBy(
                            keySelector = { it },
                            valueTransform = { textProvider.getText(it, textKey) }
                    )

    private fun getFormattedMessages(color: Color, players: List<Player>, text: String, args: Array<out Any>): Map<Locale, String> =
            players
                    .asSequence()
                    .map { it.locale }
                    .distinct()
                    .associateBy(
                            keySelector = { it },
                            valueTransform = { messageFormatter.format(it, color, text, *args) }
                    )

    private fun getFormattedTranslatedMessages(
            color: Color,
            players: List<Player>,
            textKey: TextKey,
            args: Array<out Any>
    ): Map<Locale, String> =
            players
                    .asSequence()
                    .map { it.locale }
                    .distinct()
                    .associateBy(
                            keySelector = { it },
                            valueTransform = {
                                val text = textProvider.getText(it, textKey)
                                messageFormatter.format(it, color, text, *args)
                            }
                    )

}