package ch.leadrian.samp.kamp.runtime.text

import ch.leadrian.samp.kamp.api.constants.GameTextStyle
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.api.text.TextFormatter
import ch.leadrian.samp.kamp.api.text.TextKey
import ch.leadrian.samp.kamp.api.text.TextProvider
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.registry.PlayerRegistry
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

internal class GameTextSenderImplTest {

    @Nested
    inner class SendGameTextToAllTests {

        @Test
        fun shouldSendSimpleText() {
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { gameTextForAll(any(), any(), any()) } returns true
            }
            val gameTextSender = GameTextSenderImplInjector.inject(nativeFunctionExecutor = nativeFunctionExecutor)

            gameTextSender.sendGameTextToAll(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, "Hi there")

            verify { nativeFunctionExecutor.gameTextForAll("Hi there", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value) }
        }

        @Test
        fun shouldSendFormattedText() {
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { gameTextForAll(any(), any(), any()) } returns true
            }
            val textFormatter = mockk<TextFormatter> {
                every { format(Locale.getDefault(), "Hi %s", "there") } returns "Hi there"
            }
            val gameTextSender = GameTextSenderImplInjector.inject(
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    textFormatter = textFormatter
            )

            gameTextSender.sendGameTextToAll(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, "Hi %s", "there")

            verify { nativeFunctionExecutor.gameTextForAll("Hi there", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value) }
        }

        @Test
        fun givenSeveralLocalesItShouldSendProvidedText() {
            val locale1 = Locale.GERMANY
            val player1 = mockk<Player> {
                every { this@mockk.locale } returns locale1
                every { id } returns PlayerId.valueOf(50)
            }
            val locale2 = Locale.FRANCE
            val player2 = mockk<Player> {
                every { this@mockk.locale } returns locale2
                every { id } returns PlayerId.valueOf(75)
            }
            val textKey = TextKey("test")
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { gameTextForPlayer(any(), any(), any(), any()) } returns true
            }
            val textProvider = mockk<TextProvider> {
                every { getText(locale1, textKey) } returns "Hallo"
                every { getText(locale2, textKey) } returns "Bonjour"
            }
            val playerRegistry = mockk<PlayerRegistry> {
                every { getAllPlayers() } returns listOf(player1, player2)
            }
            val gameTextSender = GameTextSenderImplInjector.inject(
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    textProvider = textProvider,
                    playerRegistry = playerRegistry
            )

            gameTextSender.sendGameTextToAll(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey)

            verify {
                nativeFunctionExecutor.gameTextForPlayer(50, "Hallo", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
                nativeFunctionExecutor.gameTextForPlayer(75, "Bonjour", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }

        @Test
        fun givenSingleLocaleItShouldSendProvidedText() {
            val locale = Locale.GERMANY
            val player1 = mockk<Player> {
                every { this@mockk.locale } returns locale
                every { id } returns PlayerId.valueOf(50)
            }
            val player2 = mockk<Player> {
                every { this@mockk.locale } returns locale
                every { id } returns PlayerId.valueOf(75)
            }
            val textKey = TextKey("test")
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { gameTextForAll(any(), any(), any()) } returns true
            }
            val textProvider = mockk<TextProvider> {
                every { getText(locale, textKey) } returns "Hallo"
            }
            val playerRegistry = mockk<PlayerRegistry> {
                every { getAllPlayers() } returns listOf(player1, player2)
            }
            val gameTextSender = GameTextSenderImplInjector.inject(
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    textProvider = textProvider,
                    playerRegistry = playerRegistry
            )

            gameTextSender.sendGameTextToAll(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey)

            verify {
                nativeFunctionExecutor.gameTextForAll("Hallo", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }

        @Test
        fun givenSeveralLocalesItShouldSendFormattedProvidedText() {
            val locale1 = Locale.GERMANY
            val player1 = mockk<Player> {
                every { this@mockk.locale } returns locale1
                every { id } returns PlayerId.valueOf(50)
            }
            val locale2 = Locale.FRANCE
            val player2 = mockk<Player> {
                every { this@mockk.locale } returns locale2
                every { id } returns PlayerId.valueOf(75)
            }
            val textKey = TextKey("test")
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { gameTextForPlayer(any(), any(), any(), any()) } returns true
            }
            val textProvider = mockk<TextProvider> {
                every { getText(locale1, textKey) } returns "Hallo %s"
                every { getText(locale2, textKey) } returns "Bonjour %s"
            }
            val playerRegistry = mockk<PlayerRegistry> {
                every { getAllPlayers() } returns listOf(player1, player2)
            }
            val textFormatter = mockk<TextFormatter> {
                every { format(locale1, "Hallo %s", "SAMP") } returns "Hallo SAMP"
                every { format(locale2, "Bonjour %s", "SAMP") } returns "Bonjour SAMP"
            }
            val gameTextSender = GameTextSenderImplInjector.inject(
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    textProvider = textProvider,
                    playerRegistry = playerRegistry,
                    textFormatter = textFormatter
            )

            gameTextSender.sendGameTextToAll(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey, "SAMP")

            verify {
                nativeFunctionExecutor.gameTextForPlayer(50, "Hallo SAMP", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
                nativeFunctionExecutor.gameTextForPlayer(75, "Bonjour SAMP", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }

        @Test
        fun givenSingleLocalesItShouldSendFormattedProvidedText() {
            val locale = Locale.GERMANY
            val player1 = mockk<Player> {
                every { this@mockk.locale } returns locale
                every { id } returns PlayerId.valueOf(50)
            }
            val player2 = mockk<Player> {
                every { this@mockk.locale } returns locale
                every { id } returns PlayerId.valueOf(75)
            }
            val textKey = TextKey("test")
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { gameTextForAll(any(), any(), any()) } returns true
            }
            val textProvider = mockk<TextProvider> {
                every { getText(locale, textKey) } returns "Hallo %s"
            }
            val playerRegistry = mockk<PlayerRegistry> {
                every { getAllPlayers() } returns listOf(player1, player2)
            }
            val textFormatter = mockk<TextFormatter> {
                every { format(locale, "Hallo %s", "SAMP") } returns "Hallo SAMP"
            }
            val gameTextSender = GameTextSenderImplInjector.inject(
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    textProvider = textProvider,
                    playerRegistry = playerRegistry,
                    textFormatter = textFormatter
            )

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
            val player = mockk<Player> {
                every { this@mockk.id } returns PlayerId.valueOf(50)
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { gameTextForPlayer(any(), any(), any(), any()) } returns true
            }
            val gameTextSender = GameTextSenderImplInjector.inject(nativeFunctionExecutor = nativeFunctionExecutor)

            gameTextSender.sendGameTextToPlayer(player, GameTextStyle.BANK_GOTHIC_CENTER_1, 13, "Hi there")

            verify {
                nativeFunctionExecutor.gameTextForPlayer(50, "Hi there", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }

        @Test
        fun shouldProvidedText() {
            val textKey = TextKey("test")
            val locale = Locale.GERMANY
            val player = mockk<Player> {
                every { this@mockk.id } returns PlayerId.valueOf(50)
                every { this@mockk.locale } returns locale
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { gameTextForPlayer(any(), any(), any(), any()) } returns true
            }
            val textProvider = mockk<TextProvider> {
                every { getText(locale, textKey) } returns "Hi there"
            }
            val gameTextSender = GameTextSenderImplInjector.inject(
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    textProvider = textProvider
            )

            gameTextSender.sendGameTextToPlayer(player, GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey)

            verify {
                nativeFunctionExecutor.gameTextForPlayer(50, "Hi there", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }

        @Test
        fun shouldSendFormattedText() {
            val locale = Locale.GERMANY
            val player = mockk<Player> {
                every { this@mockk.id } returns PlayerId.valueOf(50)
                every { this@mockk.locale } returns locale
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { gameTextForPlayer(any(), any(), any(), any()) } returns true
            }
            val textFormatter = mockk<TextFormatter> {
                every { format(locale, "Hi %s", "there") } returns "Hi there"
            }
            val gameTextSender = GameTextSenderImplInjector.inject(
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    textFormatter = textFormatter
            )

            gameTextSender.sendGameTextToPlayer(player, GameTextStyle.BANK_GOTHIC_CENTER_1, 13, "Hi %s", "there")

            verify {
                nativeFunctionExecutor.gameTextForPlayer(50, "Hi there", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }

        @Test
        fun shouldSendFormattedProvidedText() {
            val textKey = TextKey("test")
            val locale = Locale.GERMANY
            val player = mockk<Player> {
                every { this@mockk.id } returns PlayerId.valueOf(50)
                every { this@mockk.locale } returns locale
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { gameTextForPlayer(any(), any(), any(), any()) } returns true
            }
            val textFormatter = mockk<TextFormatter> {
                every { format(locale, "Hi %s", "there") } returns "Hi there"
            }
            val textProvider = mockk<TextProvider> {
                every { getText(locale, textKey) } returns "Hi %s"
            }
            val gameTextSender = GameTextSenderImplInjector.inject(
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    textFormatter = textFormatter,
                    textProvider = textProvider
            )

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
            val player1 = mockk<Player> {
                every { this@mockk.id } returns PlayerId.valueOf(50)
            }
            val player2 = mockk<Player> {
                every { this@mockk.id } returns PlayerId.valueOf(75)
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { gameTextForPlayer(any(), any(), any(), any()) } returns true
            }
            val playerRegistry = mockk<PlayerRegistry> {
                every { getAllPlayers() } returns listOf(player1, player2)
            }
            val gameTextSender = GameTextSenderImplInjector.inject(
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    playerRegistry = playerRegistry
            )

            gameTextSender.sendGameText(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, "Hi there") { it == player2 }

            verify {
                nativeFunctionExecutor.gameTextForPlayer(75, "Hi there", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }

        @Test
        fun shouldProvidedText() {
            val textKey = TextKey("test")
            val locale1 = Locale.GERMANY
            val player1 = mockk<Player> {
                every { this@mockk.id } returns PlayerId.valueOf(50)
                every { this@mockk.locale } returns locale1
            }
            val locale2 = Locale.FRANCE
            val player2 = mockk<Player> {
                every { this@mockk.id } returns PlayerId.valueOf(75)
                every { this@mockk.locale } returns locale2
            }
            val player3 = mockk<Player> {
                every { this@mockk.id } returns PlayerId.valueOf(100)
                every { this@mockk.locale } returns locale2
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { gameTextForPlayer(any(), any(), any(), any()) } returns true
            }
            val textProvider = mockk<TextProvider> {
                every { getText(locale1, textKey) } returns "Hi there"
                every { getText(locale2, textKey) } returns "Bonjour"
            }
            val playerRegistry = mockk<PlayerRegistry> {
                every { getAllPlayers() } returns listOf(player1, player2, player3)
            }
            val gameTextSender = GameTextSenderImplInjector.inject(
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    textProvider = textProvider,
                    playerRegistry = playerRegistry
            )

            gameTextSender.sendGameText(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey) { it != player2 }

            verify {
                nativeFunctionExecutor.gameTextForPlayer(50, "Hi there", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
                nativeFunctionExecutor.gameTextForPlayer(100, "Bonjour", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }

        @Test
        fun shouldSendFormattedText() {
            val player1 = mockk<Player> {
                every { this@mockk.id } returns PlayerId.valueOf(50)
            }
            val player2 = mockk<Player> {
                every { this@mockk.id } returns PlayerId.valueOf(75)
            }
            val player3 = mockk<Player> {
                every { this@mockk.id } returns PlayerId.valueOf(100)
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { gameTextForPlayer(any(), any(), any(), any()) } returns true
            }
            val textFormatter = mockk<TextFormatter> {
                every { format(Locale.getDefault(), "Hi %s", "there") } returns "Hi there"
            }
            val playerRegistry = mockk<PlayerRegistry> {
                every { getAllPlayers() } returns listOf(player1, player2, player3)
            }
            val gameTextSender = GameTextSenderImplInjector.inject(
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    textFormatter = textFormatter,
                    playerRegistry = playerRegistry
            )

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
            val player1 = mockk<Player> {
                every { this@mockk.id } returns PlayerId.valueOf(50)
                every { this@mockk.locale } returns locale1
            }
            val locale2 = Locale.FRANCE
            val player2 = mockk<Player> {
                every { this@mockk.id } returns PlayerId.valueOf(75)
                every { this@mockk.locale } returns locale2
            }
            val player3 = mockk<Player> {
                every { this@mockk.id } returns PlayerId.valueOf(100)
                every { this@mockk.locale } returns locale2
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { gameTextForPlayer(any(), any(), any(), any()) } returns true
            }
            val textFormatter = mockk<TextFormatter> {
                every { format(locale1, "Hi %s", "SAMP") } returns "Hi SAMP"
                every { format(locale2, "Bonjour %s", "SAMP") } returns "Bonjour SAMP"
            }
            val textProvider = mockk<TextProvider> {
                every { getText(locale1, textKey) } returns "Hi %s"
                every { getText(locale2, textKey) } returns "Bonjour %s"
            }
            val playerRegistry = mockk<PlayerRegistry> {
                every { getAllPlayers() } returns listOf(player1, player2, player3)
            }
            val gameTextSender = GameTextSenderImplInjector.inject(
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    textFormatter = textFormatter,
                    textProvider = textProvider,
                    playerRegistry = playerRegistry
            )

            gameTextSender.sendGameText(GameTextStyle.BANK_GOTHIC_CENTER_1, 13, textKey, "SAMP") { it != player2 }

            verify {
                nativeFunctionExecutor.gameTextForPlayer(50, "Hi SAMP", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
                nativeFunctionExecutor.gameTextForPlayer(100, "Bonjour SAMP", 13, GameTextStyle.BANK_GOTHIC_CENTER_1.value)
            }
        }
    }

}