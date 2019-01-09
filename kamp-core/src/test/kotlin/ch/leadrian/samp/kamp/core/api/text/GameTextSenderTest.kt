package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.constants.GameTextStyle
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class GameTextSenderTest {

    private lateinit var gameTextSender: GameTextSender

    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val playerRegistry = mockk<PlayerRegistry>()
    private val textPreparer = mockk<TextPreparer>()

    @BeforeEach
    fun setUp() {
        gameTextSender = GameTextSender(
                nativeFunctionExecutor = nativeFunctionExecutor,
                playerRegistry = playerRegistry,
                textPreparer = textPreparer
        )
    }

    @Nested
    inner class SendGameTextToAllTests {

        @Test
        fun shouldSendSimpleGameTextToAll() {
            every { nativeFunctionExecutor.gameTextForAll(any(), any(), any()) } returns true

            gameTextSender.sendGameTextToAll(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, "Hi there")

            verify {
                nativeFunctionExecutor.gameTextForAll(
                        text = "Hi there",
                        time = 13,
                        style = GameTextStyle.BANK_GOTHIC_CENTER_1.value
                )
            }
        }

        @Test
        fun shouldSendFormattedGameTextToEachPlayer() {
            every { nativeFunctionExecutor.gameTextForPlayer(any(), any(), any(), any()) } returns true
            val player1 = mockk<Player> {
                every { id } returns PlayerId.valueOf(50)
            }
            val player2 = mockk<Player> {
                every { id } returns PlayerId.valueOf(75)
            }
            every { textPreparer.prepareForAllPlayers("Hi %s", arrayOf("there"), any(), any()) } answers {
                thirdArg<(Player, String) -> Unit>().invoke(player1, "Hallo")
                thirdArg<(Player, String) -> Unit>().invoke(player2, "Bonjour")
            }

            gameTextSender.sendGameTextToAll(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, "Hi %s", "there")

            verify {
                nativeFunctionExecutor.gameTextForPlayer(
                        playerid = 50,
                        text = "Hallo",
                        time = 13,
                        style = GameTextStyle.BANK_GOTHIC_CENTER_1.value
                )
                nativeFunctionExecutor.gameTextForPlayer(
                        playerid = 75,
                        text = "Bonjour",
                        time = 13,
                        style = GameTextStyle.BANK_GOTHIC_CENTER_1.value
                )
            }
        }

        @Test
        fun shouldSendFormattedGameTextToAll() {
            every { nativeFunctionExecutor.gameTextForAll(any(), any(), any()) } returns true
            every { textPreparer.prepareForAllPlayers("Hi %s", arrayOf("there"), any(), any()) } answers {
                arg<(String) -> Unit>(3).invoke("Hi there")
            }

            gameTextSender.sendGameTextToAll(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, "Hi %s", "there")

            verify {
                nativeFunctionExecutor.gameTextForAll(
                        text = "Hi there",
                        time = 13,
                        style = GameTextStyle.BANK_GOTHIC_CENTER_1.value
                )
            }
        }

        @Test
        fun shouldSendTranslatedGameTextToEachPlayer() {
            every { nativeFunctionExecutor.gameTextForPlayer(any(), any(), any(), any()) } returns true
            val player1 = mockk<Player> {
                every { id } returns PlayerId.valueOf(50)
            }
            val player2 = mockk<Player> {
                every { id } returns PlayerId.valueOf(75)
            }
            val textKey = TextKey("test")
            every { textPreparer.prepareForAllPlayers(textKey, any(), any()) } answers {
                secondArg<(Player, String) -> Unit>().invoke(player1, "Hallo")
                secondArg<(Player, String) -> Unit>().invoke(player2, "Bonjour")
            }

            gameTextSender.sendGameTextToAll(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey)

            verify {
                nativeFunctionExecutor.gameTextForPlayer(
                        playerid = 50,
                        text = "Hallo",
                        time = 13,
                        style = GameTextStyle.BANK_GOTHIC_CENTER_1.value
                )
                nativeFunctionExecutor.gameTextForPlayer(
                        playerid = 75,
                        text = "Bonjour",
                        time = 13,
                        style = GameTextStyle.BANK_GOTHIC_CENTER_1.value
                )
            }
        }

        @Test
        fun shouldSendTranslatedGameTextToAll() {
            val textKey = TextKey("test")
            every { nativeFunctionExecutor.gameTextForAll(any(), any(), any()) } returns true
            every { textPreparer.prepareForAllPlayers(textKey, any(), any()) } answers {
                thirdArg<(String) -> Unit>().invoke("Hi there")
            }

            gameTextSender.sendGameTextToAll(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey)

            verify {
                nativeFunctionExecutor.gameTextForAll(
                        text = "Hi there",
                        time = 13,
                        style = GameTextStyle.BANK_GOTHIC_CENTER_1.value
                )
            }
        }

        @Test
        fun shouldSendFormattedTranslatedGameTextToEachPlayer() {
            every { nativeFunctionExecutor.gameTextForPlayer(any(), any(), any(), any()) } returns true
            val player1 = mockk<Player> {
                every { id } returns PlayerId.valueOf(50)
            }
            val player2 = mockk<Player> {
                every { id } returns PlayerId.valueOf(75)
            }
            val textKey = TextKey("test")
            every { textPreparer.prepareForAllPlayers(textKey, arrayOf("SAMP"), any(), any()) } answers {
                thirdArg<(Player, String) -> Unit>().invoke(player1, "Hallo")
                thirdArg<(Player, String) -> Unit>().invoke(player2, "Bonjour")
            }

            gameTextSender.sendGameTextToAll(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey, "SAMP")

            verify {
                nativeFunctionExecutor.gameTextForPlayer(
                        playerid = 50,
                        text = "Hallo",
                        time = 13,
                        style = GameTextStyle.BANK_GOTHIC_CENTER_1.value
                )
                nativeFunctionExecutor.gameTextForPlayer(
                        playerid = 75,
                        text = "Bonjour",
                        time = 13,
                        style = GameTextStyle.BANK_GOTHIC_CENTER_1.value
                )
            }
        }

        @Test
        fun shouldSendFormattedTranslatedGameTextToAll() {
            val textKey = TextKey("test")
            every { nativeFunctionExecutor.gameTextForAll(any(), any(), any()) } returns true
            every { textPreparer.prepareForAllPlayers(textKey, arrayOf("there"), any(), any()) } answers {
                arg<(String) -> Unit>(3).invoke("Hi there")
            }

            gameTextSender.sendGameTextToAll(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey, "there")

            verify {
                nativeFunctionExecutor.gameTextForAll(
                        text = "Hi there",
                        time = 13,
                        style = GameTextStyle.BANK_GOTHIC_CENTER_1.value
                )
            }
        }
    }

    @Nested
    inner class SendGameTextToPlayerTests {

        private val playerId = PlayerId.valueOf(50)
        private lateinit var player: Player

        @BeforeEach
        fun setUp() {
            every { nativeFunctionExecutor.gameTextForPlayer(any(), any(), any(), any()) } returns true
            player = mockk {
                every { id } returns playerId
            }
        }

        @Test
        fun shouldSendSimpleText() {
            gameTextSender.sendGameTextToPlayer(player, GameTextStyle.BANK_GOTHIC_CENTER_1, 13, "Hi there")

            verify {
                nativeFunctionExecutor.gameTextForPlayer(
                        playerid = playerId.value,
                        text = "Hi there",
                        time = 13,
                        style = GameTextStyle.BANK_GOTHIC_CENTER_1.value
                )
            }
        }

        @Test
        fun shouldSendTranslatedText() {
            val textKey = TextKey("test")
            every { textPreparer.prepareForPlayer(player, textKey, any()) } answers {
                thirdArg<(Player, String) -> String>().invoke(player, "Hi there")
            }

            gameTextSender.sendGameTextToPlayer(player, GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey)

            verify {
                nativeFunctionExecutor.gameTextForPlayer(
                        playerid = playerId.value,
                        text = "Hi there",
                        time = 13,
                        style = GameTextStyle.BANK_GOTHIC_CENTER_1.value
                )
            }
        }

        @Test
        fun shouldSendFormattedText() {
            every { textPreparer.prepareForPlayer(player, "Hi %s", arrayOf("there"), any()) } answers {
                arg<(Player, String) -> String>(3).invoke(player, "Hi there")
            }

            gameTextSender.sendGameTextToPlayer(player, GameTextStyle.BANK_GOTHIC_CENTER_1, 13, "Hi %s", "there")

            verify {
                nativeFunctionExecutor.gameTextForPlayer(
                        playerid = playerId.value,
                        text = "Hi there",
                        time = 13,
                        style = GameTextStyle.BANK_GOTHIC_CENTER_1.value
                )
            }
        }

        @Test
        fun shouldSendFormattedTranslatedText() {
            val textKey = TextKey("test")
            every { textPreparer.prepareForPlayer(player, textKey, arrayOf("there"), any()) } answers {
                arg<(Player, String) -> String>(3).invoke(player, "Hi there")
            }

            gameTextSender.sendGameTextToPlayer(player, GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey, "there")

            verify {
                nativeFunctionExecutor.gameTextForPlayer(
                        playerid = playerId.value,
                        text = "Hi there",
                        time = 13,
                        style = GameTextStyle.BANK_GOTHIC_CENTER_1.value
                )
            }
        }
    }

    @Nested
    inner class SendGameTextTests {

        private val playerId1 = PlayerId.valueOf(50)
        private lateinit var player1: Player
        private val playerId2 = PlayerId.valueOf(50)
        private lateinit var player2: Player

        @BeforeEach
        fun setUp() {
            every { nativeFunctionExecutor.gameTextForPlayer(any(), any(), any(), any()) } returns true
            player1 = mockk {
                every { id } returns playerId1
            }
            player2 = mockk {
                every { id } returns playerId2
            }
        }

        @Test
        fun shouldSendSimpleText() {
            every { playerRegistry.getAll() } returns listOf(player1, player2)

            gameTextSender.sendGameText(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, "Hi there") { it == player2 }

            verify {
                nativeFunctionExecutor.gameTextForPlayer(
                        playerid = playerId2.value,
                        text = "Hi there",
                        time = 13,
                        style = GameTextStyle.BANK_GOTHIC_CENTER_1.value
                )
            }
        }

        @Test
        fun shouldSendTranslatedText() {
            val textKey = TextKey("test")
            val playerFilter: (Player) -> Boolean = { it != player2 }
            every { textPreparer.prepare(playerFilter, textKey, any()) } answers {
                thirdArg<(Player, String) -> Unit>().invoke(player1, "Hi there")
            }

            gameTextSender.sendGameText(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey, playerFilter)

            verify {
                nativeFunctionExecutor.gameTextForPlayer(
                        playerid = playerId1.value,
                        text = "Hi there",
                        time = 13,
                        style = GameTextStyle.BANK_GOTHIC_CENTER_1.value
                )
            }
        }

        @Test
        fun shouldSendFormattedText() {
            val playerFilter: (Player) -> Boolean = { it != player2 }
            every { textPreparer.prepare(playerFilter, "Hi %s", arrayOf("there"), any()) } answers {
                arg<(Player, String) -> Unit>(3).invoke(player1, "Hi there")
            }

            gameTextSender.sendGameText(
                    GameTextStyle.BANK_GOTHIC_CENTER_1,
                    13,
                    "Hi %s",
                    "there",
                    playerFilter = playerFilter
            )

            verify {
                nativeFunctionExecutor.gameTextForPlayer(
                        playerid = playerId1.value,
                        text = "Hi there",
                        time = 13,
                        style = GameTextStyle.BANK_GOTHIC_CENTER_1.value
                )
            }
        }

        @Test
        fun shouldSendFormattedTranslatedText() {
            val textKey = TextKey("test")
            val playerFilter: (Player) -> Boolean = { it != player2 }
            every { textPreparer.prepare(playerFilter, textKey, arrayOf("there"), any()) } answers {
                arg<(Player, String) -> Unit>(3).invoke(player1, "Hi there")
            }

            gameTextSender.sendGameText(
                    GameTextStyle.BANK_GOTHIC_CENTER_1,
                    13,
                    textKey,
                    "there",
                    playerFilter = playerFilter
            )

            verify {
                nativeFunctionExecutor.gameTextForPlayer(
                        playerid = playerId1.value,
                        text = "Hi there",
                        time = 13,
                        style = GameTextStyle.BANK_GOTHIC_CENTER_1.value
                )
            }
        }
    }
}