package ch.leadrian.samp.kamp.core.api.text

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

internal class PlayerMessageSenderTest {

    private val fromPlayerId = PlayerId.valueOf(69)
    private lateinit var fromPlayer: Player
    private lateinit var playerMessageSender: PlayerMessageSender

    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val playerRegistry = mockk<PlayerRegistry>()
    private val textPreparer = mockk<TextPreparer>()

    @BeforeEach
    fun setUp() {
        fromPlayer = mockk {
            every { id } returns fromPlayerId
        }
        playerMessageSender = PlayerMessageSender(
                nativeFunctionExecutor = nativeFunctionExecutor,
                playerRegistry = playerRegistry,
                textPreparer = textPreparer
        )
    }

    @Nested
    inner class SendPlayerMessageToAllTests {

        @Test
        fun shouldSendSimpleGameTextToAll() {
            every { nativeFunctionExecutor.sendPlayerMessageToAll(any(), any()) } returns true

            playerMessageSender.sendPlayerMessageToAll(fromPlayer, "Hi there")

            verify {
                nativeFunctionExecutor.sendPlayerMessageToAll(fromPlayerId.value, "Hi there")
            }
        }

        @Test
        fun shouldSendFormattedGameTextToEachPlayer() {
            every { nativeFunctionExecutor.sendPlayerMessageToPlayer(any(), any(), any()) } returns true
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

            playerMessageSender.sendPlayerMessageToAll(fromPlayer, "Hi %s", "there")

            verify {
                nativeFunctionExecutor.sendPlayerMessageToPlayer(
                        playerid = 50,
                        senderid = fromPlayerId.value,
                        message = "Hallo"
                )
                nativeFunctionExecutor.sendPlayerMessageToPlayer(
                        playerid = 75,
                        senderid = fromPlayerId.value,
                        message = "Bonjour"
                )
            }
        }

        @Test
        fun shouldSendFormattedGameTextToAll() {
            every { nativeFunctionExecutor.sendPlayerMessageToAll(any(), any()) } returns true
            every { textPreparer.prepareForAllPlayers("Hi %s", arrayOf("there"), any(), any()) } answers {
                arg<(String) -> Unit>(3).invoke("Hi there")
            }

            playerMessageSender.sendPlayerMessageToAll(fromPlayer, "Hi %s", "there")

            verify { nativeFunctionExecutor.sendPlayerMessageToAll(fromPlayerId.value, "Hi there") }
        }

        @Test
        fun shouldSendTranslatedGameTextToEachPlayer() {
            every { nativeFunctionExecutor.sendPlayerMessageToPlayer(any(), any(), any()) } returns true
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

            playerMessageSender.sendPlayerMessageToAll(fromPlayer, textKey)

            verify {
                nativeFunctionExecutor.sendPlayerMessageToPlayer(
                        playerid = 50,
                        senderid = fromPlayerId.value,
                        message = "Hallo"
                )
                nativeFunctionExecutor.sendPlayerMessageToPlayer(
                        playerid = 75,
                        senderid = fromPlayerId.value,
                        message = "Bonjour"
                )
            }
        }

        @Test
        fun shouldSendTranslatedGameTextToAll() {
            val textKey = TextKey("test")
            every { nativeFunctionExecutor.sendPlayerMessageToAll(any(), any()) } returns true
            every { textPreparer.prepareForAllPlayers(textKey, any(), any()) } answers {
                thirdArg<(String) -> Unit>().invoke("Hi there")
            }

            playerMessageSender.sendPlayerMessageToAll(fromPlayer, textKey)

            verify { nativeFunctionExecutor.sendPlayerMessageToAll(fromPlayerId.value, "Hi there") }
        }

        @Test
        fun shouldSendFormattedTranslatedGameTextToEachPlayer() {
            every { nativeFunctionExecutor.sendPlayerMessageToPlayer(any(), any(), any()) } returns true
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

            playerMessageSender.sendPlayerMessageToAll(fromPlayer, textKey, "SAMP")

            verify {
                nativeFunctionExecutor.sendPlayerMessageToPlayer(
                        playerid = 50,
                        senderid = fromPlayerId.value,
                        message = "Hallo SAMP"
                )
                nativeFunctionExecutor.sendPlayerMessageToPlayer(
                        playerid = 75,
                        senderid = fromPlayerId.value,
                        message = "Bonjour SAMP"
                )
            }
        }

        @Test
        fun shouldSendFormattedTranslatedGameTextToAll() {
            val textKey = TextKey("test")
            every { nativeFunctionExecutor.sendPlayerMessageToAll(any(), any()) } returns true
            every { textPreparer.prepareForAllPlayers(textKey, arrayOf("there"), any(), any()) } answers {
                arg<(String) -> Unit>(3).invoke("Hi there")
            }

            playerMessageSender.sendPlayerMessageToAll(fromPlayer, textKey, "there")

            verify { nativeFunctionExecutor.sendPlayerMessageToAll(fromPlayerId.value, "Hi there") }
        }
    }

    @Nested
    inner class SendPlayerMessageToPlayerTests {

        private val playerId = PlayerId.valueOf(50)
        private lateinit var player: Player

        @BeforeEach
        fun setUp() {
            every { nativeFunctionExecutor.sendPlayerMessageToPlayer(any(), any(), any()) } returns true
            player = mockk {
                every { id } returns playerId
            }
        }

        @Test
        fun shouldSendSimpleText() {
            playerMessageSender.sendPlayerMessageToPlayer(player, fromPlayer, "Hi there")

            verify {
                nativeFunctionExecutor.sendPlayerMessageToPlayer(
                        playerid = playerId.value,
                        senderid = fromPlayerId.value,
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

            playerMessageSender.sendPlayerMessageToPlayer(player, fromPlayer, textKey)

            verify {
                nativeFunctionExecutor.sendPlayerMessageToPlayer(
                        playerid = playerId.value,
                        senderid = fromPlayerId.value,
                        message = "Hi there"
                )
            }
        }

        @Test
        fun shouldSendFormattedText() {
            every { textPreparer.prepareForPlayer(player, "Hi %s", arrayOf("there"), any()) } answers {
                arg<(Player, String) -> String>(3).invoke(player, "Hi there")
            }

            playerMessageSender.sendPlayerMessageToPlayer(player, fromPlayer, "Hi %s", "there")

            verify {
                nativeFunctionExecutor.sendPlayerMessageToPlayer(
                        playerid = playerId.value,
                        senderid = fromPlayerId.value,
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

            playerMessageSender.sendPlayerMessageToPlayer(player, fromPlayer, textKey, "there")

            verify {
                nativeFunctionExecutor.sendPlayerMessageToPlayer(
                        playerid = playerId.value,
                        senderid = fromPlayerId.value,
                        message = "Hi there"
                )
            }
        }
    }

    @Nested
    inner class SendPlayerMessageTests {

        private val playerId1 = PlayerId.valueOf(50)
        private lateinit var player1: Player
        private val playerId2 = PlayerId.valueOf(50)
        private lateinit var player2: Player

        @BeforeEach
        fun setUp() {
            every { nativeFunctionExecutor.sendPlayerMessageToPlayer(any(), any(), any()) } returns true
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

            playerMessageSender.sendPlayerMessage(fromPlayer, "Hi there") { it != player2 }

            verify {
                nativeFunctionExecutor.sendPlayerMessageToPlayer(
                        playerid = playerId1.value,
                        senderid = fromPlayerId.value,
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

            playerMessageSender.sendPlayerMessage(fromPlayer, textKey, playerFilter)

            verify {
                nativeFunctionExecutor.sendPlayerMessageToPlayer(
                        playerid = playerId1.value,
                        senderid = fromPlayerId.value,
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

            playerMessageSender.sendPlayerMessage(fromPlayer, "Hi %s", "there", playerFilter = playerFilter)

            verify {
                nativeFunctionExecutor.sendPlayerMessageToPlayer(
                        playerid = playerId1.value,
                        senderid = fromPlayerId.value,
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

            playerMessageSender.sendPlayerMessage(fromPlayer, textKey, "there", playerFilter = playerFilter)

            verify {
                nativeFunctionExecutor.sendPlayerMessageToPlayer(
                        playerid = playerId1.value,
                        senderid = fromPlayerId.value,
                        message = "Hi there"
                )
            }
        }
    }
}