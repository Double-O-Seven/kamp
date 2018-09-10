package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.constants.GameTextStyle
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import java.util.*
import javax.inject.Inject

class GameTextSender
@Inject
internal constructor(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val textProvider: TextProvider,
        private val playerRegistry: PlayerRegistry,
        private val textFormatter: TextFormatter
) {

    fun sendGameTextToAll(style: GameTextStyle, time: Int, text: String) {
        nativeFunctionExecutor.gameTextForAll(text = text, time = time, style = style.value)
    }

    fun sendGameTextToAll(style: GameTextStyle, time: Int, text: String, vararg args: Any) {
        val formattedText = textFormatter.format(Locale.getDefault(), text, *args)
        nativeFunctionExecutor.gameTextForAll(text = formattedText, time = time, style = style.value)
    }

    fun sendGameTextToAll(style: GameTextStyle, time: Int, textKey: TextKey) {
        val allPlayers = playerRegistry.getAll()
        val translatedTexts = getTranslatedTexts(allPlayers, textKey)
        sendGameTextToAll(style, time, allPlayers, translatedTexts)
    }

    fun sendGameTextToAll(style: GameTextStyle, time: Int, textKey: TextKey, vararg args: Any) {
        val allPlayers = playerRegistry.getAll()
        val formattedTexts = getFormattedTexts(allPlayers, textKey, args)
        sendGameTextToAll(style, time, allPlayers, formattedTexts)
    }

    private fun getTranslatedTexts(allPlayers: List<Player>, textKey: TextKey): Map<Locale, String> =
            allPlayers
                    .asSequence()
                    .map { it.locale }
                    .distinct()
                    .associateBy(
                            keySelector = { it },
                            valueTransform = { textProvider.getText(it, textKey) }
                    )

    private fun sendGameTextToAll(style: GameTextStyle, time: Int, allPlayers: List<Player>, translatedTexts: Map<Locale, String>) {
        if (translatedTexts.size == 1) {
            val translatedText = translatedTexts.values.first()
            nativeFunctionExecutor.gameTextForAll(text = translatedText, time = time, style = style.value)
        } else {
            allPlayers.forEach {
                nativeFunctionExecutor.gameTextForPlayer(
                        playerid = it.id.value,
                        text = translatedTexts[it.locale]!!,
                        time = time,
                        style = style.value
                )
            }
        }
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
        val formattedText = textFormatter.format(player.locale, text, *args)
        nativeFunctionExecutor.gameTextForPlayer(
                playerid = player.id.value,
                text = formattedText,
                time = time,
                style = style.value
        )
    }

    fun sendGameTextToPlayer(player: Player, style: GameTextStyle, time: Int, textKey: TextKey) {
        val text = textProvider.getText(player.locale, textKey)
        nativeFunctionExecutor.gameTextForPlayer(
                playerid = player.id.value,
                text = text,
                time = time,
                style = style.value
        )
    }

    fun sendGameTextToPlayer(player: Player, style: GameTextStyle, time: Int, textKey: TextKey, vararg args: Any) {
        val text = textProvider.getText(player.locale, textKey)
        val formattedText = textFormatter.format(player.locale, text, *args)
        nativeFunctionExecutor.gameTextForPlayer(
                playerid = player.id.value,
                text = formattedText,
                time = time,
                style = style.value
        )
    }

    fun sendGameText(style: GameTextStyle, time: Int, text: String, playerFilter: (Player) -> Boolean) {
        playerRegistry
                .getAll()
                .asSequence()
                .filter { playerFilter(it) }
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
        playerRegistry
                .getAll()
                .asSequence()
                .filter { playerFilter(it) }
                .forEach {
                    val text = textProvider.getText(it.locale, textKey)
                    nativeFunctionExecutor.gameTextForPlayer(
                            playerid = it.id.value,
                            text = text,
                            time = time,
                            style = style.value
                    )
                }
    }

    fun sendGameText(style: GameTextStyle, time: Int, text: String, vararg args: Any, playerFilter: (Player) -> Boolean) {
        val formattedText = textFormatter.format(Locale.getDefault(), text, *args)
        playerRegistry
                .getAll()
                .asSequence()
                .filter { playerFilter(it) }
                .forEach {
                    nativeFunctionExecutor.gameTextForPlayer(
                            playerid = it.id.value,
                            text = formattedText,
                            time = time,
                            style = style.value
                    )
                }
    }

    fun sendGameText(style: GameTextStyle, time: Int, textKey: TextKey, vararg args: Any, playerFilter: (Player) -> Boolean) {
        val players = playerRegistry.getAll().filter { playerFilter(it) }
        val formattedTexts = getFormattedTexts(players, textKey, args)
        players.forEach {
            nativeFunctionExecutor.gameTextForPlayer(
                    playerid = it.id.value,
                    text = formattedTexts[it.locale]!!,
                    time = time,
                    style = style.value
            )
        }
    }

    private fun getFormattedTexts(allPlayers: List<Player>, textKey: TextKey, args: Array<out Any>): Map<Locale, String> =
            allPlayers
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