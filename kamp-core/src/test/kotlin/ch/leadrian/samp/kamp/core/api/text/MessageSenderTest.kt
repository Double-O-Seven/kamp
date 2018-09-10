package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.data.Colors
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

internal class MessageSenderTest {

    private lateinit var messageSender: MessageSender

    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val playerRegistry = mockk<PlayerRegistry>()
    private val textPreparer = mockk<TextPreparer>()

    @BeforeEach
    fun setUp() {
        messageSender = MessageSender(
                nativeFunctionExecutor = nativeFunctionExecutor,
                playerRegistry = playerRegistry,
                textPreparer = textPreparer
        )
    }

    @Nested
    inner class SendMessageToAllTests {

        @Test
        fun shouldSendSimpleGameTextToAll() {
            every { nativeFunctionExecutor.sendClientMessageToAll(any(), any()) } returns true

            messageSender.sendMessageToAll(Colors.RED, "Hi there")

            verify {
                nativeFunctionExecutor.sendClientMessageToAll(Colors.RED.value, "Hi there")
            }
        }

        @Test
        fun shouldSendFormattedGameTextToEachPlayer() {
            every { nativeFunctionExecutor.sendClientMessage(any(), any(), any()) } returns true
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

            messageSender.sendMessageToAll(Colors.RED, "Hi %s", "there")

            verify {
                nativeFunctionExecutor.sendClientMessage(playerid = 50, color = Colors.RED.value, message = "Hallo")
                nativeFunctionExecutor.sendClientMessage(playerid = 75, color = Colors.RED.value, message = "Bonjour")
            }
        }

        @Test
        fun shouldSendFormattedGameTextToAll() {
            every { nativeFunctionExecutor.sendClientMessageToAll(any(), any()) } returns true
            every { textPreparer.prepareForAllPlayers("Hi %s", arrayOf("there"), any(), any()) } answers {
                arg<(String) -> Unit>(3).invoke("Hi there")
            }

            messageSender.sendMessageToAll(Colors.RED, "Hi %s", "there")

            verify { nativeFunctionExecutor.sendClientMessageToAll(Colors.RED.value, "Hi there") }
        }

        @Test
        fun shouldSendTranslatedGameTextToEachPlayer() {
            every { nativeFunctionExecutor.sendClientMessage(any(), any(), any()) } returns true
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

            messageSender.sendMessageToAll(Colors.RED, textKey)

            verify {
                nativeFunctionExecutor.sendClientMessage(playerid = 50, color = Colors.RED.value, message = "Hallo")
                nativeFunctionExecutor.sendClientMessage(playerid = 75, color = Colors.RED.value, message = "Bonjour")
            }
        }

        @Test
        fun shouldSendTranslatedGameTextToAll() {
            val textKey = TextKey("test")
            every { nativeFunctionExecutor.sendClientMessageToAll(any(), any()) } returns true
            every { textPreparer.prepareForAllPlayers(textKey, any(), any()) } answers {
                thirdArg<(String) -> Unit>().invoke("Hi there")
            }

            messageSender.sendMessageToAll(Colors.RED, textKey)

            verify { nativeFunctionExecutor.sendClientMessageToAll(Colors.RED.value, "Hi there") }
        }

        @Test
        fun shouldSendFormattedTranslatedGameTextToEachPlayer() {
            every { nativeFunctionExecutor.sendClientMessage(any(), any(), any()) } returns true
            val player1 = mockk<Player> {
                every { id } returns PlayerId.valueOf(50)
            }
            val player2 = mockk<Player> {
                every { id } returns PlayerId.valueOf(75)
            }
            val textKey = TextKey("test")
            every { textPreparer.prepareForAllPlayers(textKey, arrayOf("SAMP"), any(), any()) } answers {
                thirdArg<(Player, String) -> Unit>().invoke(player1, "Hallo SAMP")
                thirdArg<(Player, String) -> Unit>().invoke(player2, "Bonjour SAMP")
            }

            messageSender.sendMessageToAll(Colors.RED, textKey, "SAMP")

            verify {
                nativeFunctionExecutor.sendClientMessage(playerid = 50, color = Colors.RED.value, message = "Hallo SAMP")
                nativeFunctionExecutor.sendClientMessage(playerid = 75, color = Colors.RED.value, message = "Bonjour SAMP")
            }
        }

        @Test
        fun shouldSendFormattedTranslatedGameTextToAll() {
            val textKey = TextKey("test")
            every { nativeFunctionExecutor.sendClientMessageToAll(any(), any()) } returns true
            every { textPreparer.prepareForAllPlayers(textKey, arrayOf("there"), any(), any()) } answers {
                arg<(String) -> Unit>(3).invoke("Hi there")
            }

            messageSender.sendMessageToAll(Colors.RED, textKey, "there")

            verify { nativeFunctionExecutor.sendClientMessageToAll(Colors.RED.value, "Hi there") }
        }
    }

    @Nested
    inner class SendMessageToPlayerTests {

        private val playerId = PlayerId.valueOf(50)
        private lateinit var player: Player

        @BeforeEach
        fun setUp() {
            every { nativeFunctionExecutor.sendClientMessage(any(), any(), any()) } returns true
            player = mockk {
                every { id } returns playerId
            }
        }

        @Test
        fun shouldSendSimpleText() {
            messageSender.sendMessageToPlayer(player, Colors.RED, "Hi there")

            verify {
                nativeFunctionExecutor.sendClientMessage(
                        playerid = playerId.value,
                        color = Colors.RED.value,
                        message = "Hi there"
                )
            }
        }

        @Test
        fun shouldSendTranslatedText() {
            val textKey = TextKey("test")
            every { textPreparer.prepareForPlayer(player, textKey, any()) } answers {
                thirdArg<(Player, String) -> String>().invoke(player, "Hi there")
            }

            messageSender.sendMessageToPlayer(player, Colors.RED, textKey)

            verify {
                nativeFunctionExecutor.sendClientMessage(
                        playerid = playerId.value,
                        color = Colors.RED.value,
                        message = "Hi there"
                )
            }
        }

        @Test
        fun shouldSendFormattedText() {
            every { textPreparer.prepareForPlayer(player, "Hi %s", arrayOf("there"), any()) } answers {
                arg<(Player, String) -> String>(3).invoke(player, "Hi there")
            }

            messageSender.sendMessageToPlayer(player, Colors.RED, "Hi %s", "there")

            verify {
                nativeFunctionExecutor.sendClientMessage(
                        playerid = playerId.value,
                        color = Colors.RED.value,
                        message = "Hi there"
                )
            }
        }

        @Test
        fun shouldSendFormattedTranslatedText() {
            val textKey = TextKey("test")
            every { textPreparer.prepareForPlayer(player, textKey, arrayOf("there"), any()) } answers {
                arg<(Player, String) -> String>(3).invoke(player, "Hi there")
            }

            messageSender.sendMessageToPlayer(player, Colors.RED, textKey, "there")

            verify {
                nativeFunctionExecutor.sendClientMessage(
                        playerid = playerId.value,
                        color = Colors.RED.value,
                        message = "Hi there"
                )
            }
        }
    }

    @Nested
    inner class SendMessageTests {

        private val playerId1 = PlayerId.valueOf(50)
        private lateinit var player1: Player
        private val playerId2 = PlayerId.valueOf(50)
        private lateinit var player2: Player

        @BeforeEach
        fun setUp() {
            every { nativeFunctionExecutor.sendClientMessage(any(), any(), any()) } returns true
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

            messageSender.sendMessage(Colors.RED, "Hi there") { it != player2 }

            verify {
                nativeFunctionExecutor.sendClientMessage(
                        playerid = playerId1.value,
                        color = Colors.RED.value,
                        message = "Hi there"
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

            messageSender.sendMessage(Colors.RED, textKey, playerFilter)

            verify {
                nativeFunctionExecutor.sendClientMessage(
                        playerid = playerId1.value,
                        color = Colors.RED.value,
                        message = "Hi there"
                )
            }
        }

        @Test
        fun shouldSendFormattedText() {
            val playerFilter: (Player) -> Boolean = { it != player2 }
            every { textPreparer.prepare(playerFilter, "Hi %s", arrayOf("there"), any()) } answers {
                arg<(Player, String) -> Unit>(3).invoke(player1, "Hi there")
            }

            messageSender.sendMessage(Colors.RED, "Hi %s", "there", playerFilter = playerFilter)

            verify {
                nativeFunctionExecutor.sendClientMessage(
                        playerid = playerId1.value,
                        color = Colors.RED.value,
                        message = "Hi there"
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

            messageSender.sendMessage(Colors.RED, textKey, "there", playerFilter = playerFilter)

            verify {
                nativeFunctionExecutor.sendClientMessage(
                        playerid = playerId1.value,
                        color = Colors.RED.value,
                        message = "Hi there"
                )
            }
        }
    }
}