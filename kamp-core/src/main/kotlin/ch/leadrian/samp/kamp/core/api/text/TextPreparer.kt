package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import java.util.*
import javax.inject.Inject

class TextPreparer
@Inject
internal constructor(
        private val playerRegistry: PlayerRegistry,
        private val textProvider: TextProvider,
        private val textFormatter: TextFormatter
) {

    fun prepareForAllPlayers(
            text: String,
            args: Array<out Any>,
            consumer: (Player, String) -> Unit,
            consumerForAll: (String) -> Unit
    ) {
        val players = playerRegistry.getAll()
        val formattedTexts = getFormattedTexts(players, text, args)
        prepare(players, formattedTexts, consumer, consumerForAll)
    }

    fun prepareForAllPlayers(
            textKey: TextKey,
            consumer: (Player, String) -> Unit,
            consumerForAll: (String) -> Unit
    ) {
        val players = playerRegistry.getAll()
        val translatedTexts = getTranslatedText(players, textKey)
        prepare(players, translatedTexts, consumer, consumerForAll)
    }

    fun prepareForAllPlayers(
            textKey: TextKey,
            args: Array<out Any>,
            consumer: (Player, String) -> Unit,
            consumerForAll: (String) -> Unit
    ) {
        val players = playerRegistry.getAll()
        val formattedTranslatedTexts = getFormattedTranslatedTexts(players, textKey, args)
        prepare(players, formattedTranslatedTexts, consumer, consumerForAll)
    }

    fun prepareForPlayer(
            player: Player,
            text: String,
            args: Array<out Any>,
            consumer: (Player, String) -> Unit
    ) {
        val formattedText = textFormatter.format(player.locale, text, *args)
        consumer(player, formattedText)
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
            player: Player,
            textKey: TextKey,
            args: Array<out Any>,
            consumer: (Player, String) -> Unit
    ) {
        val text = textProvider.getText(player.locale, textKey)
        prepareForPlayer(player, text, args, consumer)
    }

    fun prepare(
            playerFilter: (Player) -> Boolean,
            text: String,
            args: Array<out Any>,
            consumer: (Player, String) -> Unit
    ) {
        val players = playerRegistry.getAll().filter(playerFilter)
        val formattedTexts = getFormattedTexts(players, text, args)
        prepare(players, formattedTexts, consumer)
    }

    fun prepare(
            playerFilter: (Player) -> Boolean,
            textKey: TextKey,
            consumer: (Player, String) -> Unit
    ) {
        val players = playerRegistry.getAll().filter(playerFilter)
        val translatedTexts = getTranslatedText(players, textKey)
        prepare(players, translatedTexts, consumer)
    }

    fun prepare(
            playerFilter: (Player) -> Boolean,
            textKey: TextKey,
            args: Array<out Any>,
            consumer: (Player, String) -> Unit
    ) {
        val players = playerRegistry.getAll().filter(playerFilter)
        val formattedTranslatedTexts = getFormattedTranslatedTexts(players, textKey, args)
        prepare(players, formattedTranslatedTexts, consumer)
    }

    private fun prepare(
            players: List<Player>,
            translatedTexts: Map<Locale, String>,
            consumer: (Player, String) -> Unit,
            consumerForAll: ((String) -> Unit)? = null
    ) {
        if (consumerForAll != null && translatedTexts.size == 1) {
            consumerForAll(translatedTexts.values.first())
        } else {
            players.forEach { consumer(it, translatedTexts[it.locale]!!) }
        }
    }

    private fun getTranslatedText(players: List<Player>, textKey: TextKey): Map<Locale, String> =
            players
                    .asSequence()
                    .map { it.locale }
                    .distinct()
                    .associateBy(
                            keySelector = { it },
                            valueTransform = { textProvider.getText(it, textKey) }
                    )

    private fun getFormattedTexts(players: List<Player>, text: String, args: Array<out Any>): Map<Locale, String> =
            players
                    .asSequence()
                    .map { it.locale }
                    .distinct()
                    .associateBy(
                            keySelector = { it },
                            valueTransform = { textFormatter.format(it, text, *args) }
                    )

    private fun getFormattedTranslatedTexts(players: List<Player>, textKey: TextKey, args: Array<out Any>): Map<Locale, String> =
            players
                    .asSequence()
                    .map { it.locale }
                    .distinct()
                    .associateBy(
                            keySelector = { it },
                            valueTransform = {
                                val text = textProvider.getText(it, textKey)
                                textFormatter.format(it, text, *args)
                            }
                    )

}