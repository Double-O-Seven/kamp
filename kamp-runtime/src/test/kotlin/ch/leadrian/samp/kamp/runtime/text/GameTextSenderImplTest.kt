package ch.leadrian.samp.kamp.runtime.text

import ch.leadrian.samp.kamp.api.constants.GameTextStyle
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.api.text.TextFormatter
import ch.leadrian.samp.kamp.api.text.TextKey
import ch.leadrian.samp.kamp.api.text.TextProvider
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.PlayerImpl
import ch.leadrian.samp.kamp.runtime.entity.registry.PlayerRegistry
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Locale

internal class GameTextSenderImplTest {

    private lateinit var gameTextSender: GameTextSenderImpl

    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val textProvider = mockk<TextProvider>()
    private val playerRegistry = mockk<PlayerRegistry>()
    private val textFormatter = mockk<TextFormatter>()

    @BeforeEach
    fun setUp() {
        gameTextSender = GameTextSenderImpl(
                nativeFunctionExecutor = nativeFunctionExecutor,
                textProvider = textProvider,
                playerRegistry = playerRegistry,
                textFormatter = textFormatter
        )
    }

    @Nested
    inner class SendGameTextToAllTests {

        @Test
        fun shouldSendSimpleText() {
            every { nativeFunctionExecutor.gameTextForAll(any(), any(), any()) } returns true

            gameTextSender.sendGameTextToAll(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, "Hi there")

            verify { nativeFunctionExecutor.gameTextForAll("Hi there", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value) }
        }

        @Test
        fun shouldSendFormattedText() {
            every { nativeFunctionExecutor.gameTextForAll(any(), any(), any()) } returns true
            every { textFormatter.format(Locale.getDefault(), "Hi %s", "there") } returns "Hi there"

            gameTextSender.sendGameTextToAll(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, "Hi %s", "there")

            verify { nativeFunctionExecutor.gameTextForAll("Hi there", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value) }
        }

        @Test
        fun givenSeveralLocalesItShouldSendProvidedText() {
            val locale1 = Locale.GERMANY
            val player1 = mockk<PlayerImpl> {
                every { this@mockk.locale } returns locale1
                every { id } returns PlayerId.valueOf(50)
            }
            val locale2 = Locale.FRANCE
            val player2 = mockk<PlayerImpl> {
                every { this@mockk.locale } returns locale2
                every { id } returns PlayerId.valueOf(75)
            }
            val textKey = TextKey("test")
            every { nativeFunctionExecutor.gameTextForPlayer(any(), any(), any(), any()) } returns true
            every { textProvider.getText(locale1, textKey) } returns "Hallo"
            every { textProvider.getText(locale2, textKey) } returns "Bonjour"
            every { playerRegistry.getAll() } returns listOf(player1, player2)

            gameTextSender.sendGameTextToAll(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey)

            verify {
                nativeFunctionExecutor.gameTextForPlayer(50, "Hallo", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
                nativeFunctionExecutor.gameTextForPlayer(75, "Bonjour", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }

        @Test
        fun givenSingleLocaleItShouldSendProvidedText() {
            val locale = Locale.GERMANY
            val player1 = mockk<PlayerImpl> {
                every { this@mockk.locale } returns locale
                every { id } returns PlayerId.valueOf(50)
            }
            val player2 = mockk<PlayerImpl> {
                every { this@mockk.locale } returns locale
                every { id } returns PlayerId.valueOf(75)
            }
            val textKey = TextKey("test")
            every { nativeFunctionExecutor.gameTextForAll(any(), any(), any()) } returns true
            every { textProvider.getText(locale, textKey) } returns "Hallo"
            every { playerRegistry.getAll() } returns listOf(player1, player2)

            gameTextSender.sendGameTextToAll(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey)

            verify {
                nativeFunctionExecutor.gameTextForAll("Hallo", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }

        @Test
        fun givenSeveralLocalesItShouldSendFormattedProvidedText() {
            val locale1 = Locale.GERMANY
            val player1 = mockk<PlayerImpl> {
                every { this@mockk.locale } returns locale1
                every { id } returns PlayerId.valueOf(50)
            }
            val locale2 = Locale.FRANCE
            val player2 = mockk<PlayerImpl> {
                every { this@mockk.locale } returns locale2
                every { id } returns PlayerId.valueOf(75)
            }
            val textKey = TextKey("test")
            every { nativeFunctionExecutor.gameTextForPlayer(any(), any(), any(), any()) } returns true
            every { textProvider.getText(locale1, textKey) } returns "Hallo %s"
            every { textProvider.getText(locale2, textKey) } returns "Bonjour %s"
            every { playerRegistry.getAll() } returns listOf(player1, player2)
            every { textFormatter.format(locale1, "Hallo %s", "SAMP") } returns "Hallo SAMP"
            every { textFormatter.format(locale2, "Bonjour %s", "SAMP") } returns "Bonjour SAMP"

            gameTextSender.sendGameTextToAll(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey, "SAMP")

            verify {
                nativeFunctionExecutor.gameTextForPlayer(50, "Hallo SAMP", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
                nativeFunctionExecutor.gameTextForPlayer(75, "Bonjour SAMP", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }

        @Test
        fun givenSingleLocalesItShouldSendFormattedProvidedText() {
            val locale = Locale.GERMANY
            val player1 = mockk<PlayerImpl> {
                every { this@mockk.locale } returns locale
                every { id } returns PlayerId.valueOf(50)
            }
            val player2 = mockk<PlayerImpl> {
                every { this@mockk.locale } returns locale
                every { id } returns PlayerId.valueOf(75)
            }
            val textKey = TextKey("test")
            every { nativeFunctionExecutor.gameTextForAll(any(), any(), any()) } returns true
            every { textProvider.getText(locale, textKey) } returns "Hallo %s"
            every { playerRegistry.getAll() } returns listOf(player1, player2)
            every { textFormatter.format(locale, "Hallo %s", "SAMP") } returns "Hallo SAMP"

            gameTextSender.sendGameTextToAll(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey, "SAMP")

            verify {
                nativeFunctionExecutor.gameTextForAll("Hallo SAMP", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }
    }

    @Nested
    inner class SendGameTextToPlayerTests {

        @Test
        fun shouldSendSimpleText() {
            val player = mockk<PlayerImpl> {
                every { this@mockk.id } returns PlayerId.valueOf(50)
            }
            every { nativeFunctionExecutor.gameTextForPlayer(any(), any(), any(), any()) } returns true

            gameTextSender.sendGameTextToPlayer(player, GameTextStyle.BANK_GOTHIC_CENTER_1, 13, "Hi there")

            verify {
                nativeFunctionExecutor.gameTextForPlayer(50, "Hi there", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }

        @Test
        fun shouldProvidedText() {
            val textKey = TextKey("test")
            val locale = Locale.GERMANY
            val player = mockk<PlayerImpl> {
                every { this@mockk.id } returns PlayerId.valueOf(50)
                every { this@mockk.locale } returns locale
            }
            every { nativeFunctionExecutor.gameTextForPlayer(any(), any(), any(), any()) } returns true
            every { textProvider.getText(locale, textKey) } returns "Hi there"

            gameTextSender.sendGameTextToPlayer(player, GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey)

            verify {
                nativeFunctionExecutor.gameTextForPlayer(50, "Hi there", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }

        @Test
        fun shouldSendFormattedText() {
            val locale = Locale.GERMANY
            val player = mockk<PlayerImpl> {
                every { this@mockk.id } returns PlayerId.valueOf(50)
                every { this@mockk.locale } returns locale
            }
            every { nativeFunctionExecutor.gameTextForPlayer(any(), any(), any(), any()) } returns true
            every { textFormatter.format(locale, "Hi %s", "there") } returns "Hi there"

            gameTextSender.sendGameTextToPlayer(player, GameTextStyle.BANK_GOTHIC_CENTER_1, 13, "Hi %s", "there")

            verify {
                nativeFunctionExecutor.gameTextForPlayer(50, "Hi there", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }

        @Test
        fun shouldSendFormattedProvidedText() {
            val textKey = TextKey("test")
            val locale = Locale.GERMANY
            val player = mockk<PlayerImpl> {
                every { this@mockk.id } returns PlayerId.valueOf(50)
                every { this@mockk.locale } returns locale
            }
            every { nativeFunctionExecutor.gameTextForPlayer(any(), any(), any(), any()) } returns true
            every { textFormatter.format(locale, "Hi %s", "there") } returns "Hi there"
            every { textProvider.getText(locale, textKey) } returns "Hi %s"

            gameTextSender.sendGameTextToPlayer(player, GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey, "there")

            verify {
                nativeFunctionExecutor.gameTextForPlayer(50, "Hi there", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }
    }

    @Nested
    inner class SendGameTextTests {

        @Test
        fun shouldSendSimpleText() {
            val player1 = mockk<PlayerImpl> {
                every { this@mockk.id } returns PlayerId.valueOf(50)
            }
            val player2 = mockk<PlayerImpl> {
                every { this@mockk.id } returns PlayerId.valueOf(75)
            }
            every { nativeFunctionExecutor.gameTextForPlayer(any(), any(), any(), any()) } returns true
            every { playerRegistry.getAll() } returns listOf(player1, player2)

            gameTextSender.sendGameText(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, "Hi there") { it == player2 }

            verify {
                nativeFunctionExecutor.gameTextForPlayer(75, "Hi there", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }

        @Test
        fun shouldProvidedText() {
            val textKey = TextKey("test")
            val locale1 = Locale.GERMANY
            val player1 = mockk<PlayerImpl> {
                every { this@mockk.id } returns PlayerId.valueOf(50)
                every { this@mockk.locale } returns locale1
            }
            val locale2 = Locale.FRANCE
            val player2 = mockk<PlayerImpl> {
                every { this@mockk.id } returns PlayerId.valueOf(75)
                every { this@mockk.locale } returns locale2
            }
            val player3 = mockk<PlayerImpl> {
                every { this@mockk.id } returns PlayerId.valueOf(100)
                every { this@mockk.locale } returns locale2
            }
            every { nativeFunctionExecutor.gameTextForPlayer(any(), any(), any(), any()) } returns true
            every { textProvider.getText(locale1, textKey) } returns "Hi there"
            every { textProvider.getText(locale2, textKey) } returns "Bonjour"
            every { playerRegistry.getAll() } returns listOf(player1, player2, player3)

            gameTextSender.sendGameText(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey) { it != player2 }

            verify {
                nativeFunctionExecutor.gameTextForPlayer(50, "Hi there", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
                nativeFunctionExecutor.gameTextForPlayer(100, "Bonjour", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }

        @Test
        fun shouldSendFormattedText() {
            val player1 = mockk<PlayerImpl> {
                every { this@mockk.id } returns PlayerId.valueOf(50)
            }
            val player2 = mockk<PlayerImpl> {
                every { this@mockk.id } returns PlayerId.valueOf(75)
            }
            val player3 = mockk<PlayerImpl> {
                every { this@mockk.id } returns PlayerId.valueOf(100)
            }
            every { nativeFunctionExecutor.gameTextForPlayer(any(), any(), any(), any()) } returns true
            every { textFormatter.format(Locale.getDefault(), "Hi %s", "there") } returns "Hi there"
            every { playerRegistry.getAll() } returns listOf(player1, player2, player3)

            gameTextSender.sendGameText(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, "Hi %s", "there") { it != player2 }

            verify {
                nativeFunctionExecutor.gameTextForPlayer(50, "Hi there", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
                nativeFunctionExecutor.gameTextForPlayer(100, "Hi there", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }

        @Test
        fun shouldSendFormattedProvidedText() {
            val textKey = TextKey("test")
            val locale1 = Locale.GERMANY
            val player1 = mockk<PlayerImpl> {
                every { this@mockk.id } returns PlayerId.valueOf(50)
                every { this@mockk.locale } returns locale1
            }
            val locale2 = Locale.FRANCE
            val player2 = mockk<PlayerImpl> {
                every { this@mockk.id } returns PlayerId.valueOf(75)
                every { this@mockk.locale } returns locale2
            }
            val player3 = mockk<PlayerImpl> {
                every { this@mockk.id } returns PlayerId.valueOf(100)
                every { this@mockk.locale } returns locale2
            }
            every { nativeFunctionExecutor.gameTextForPlayer(any(), any(), any(), any()) } returns true
            every { textFormatter.format(locale1, "Hi %s", "SAMP") } returns "Hi SAMP"
            every { textFormatter.format(locale2, "Bonjour %s", "SAMP") } returns "Bonjour SAMP"
            every { textProvider.getText(locale1, textKey) } returns "Hi %s"
            every { textProvider.getText(locale2, textKey) } returns "Bonjour %s"
            every { playerRegistry.getAll() } returns listOf(player1, player2, player3)

            gameTextSender.sendGameText(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey, "SAMP") { it != player2 }

            verify {
                nativeFunctionExecutor.gameTextForPlayer(50, "Hi SAMP", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
                nativeFunctionExecutor.gameTextForPlayer(100, "Bonjour SAMP", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }
    }

}