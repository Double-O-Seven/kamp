package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

internal class TextPreparerTest {

    private lateinit var textPreparer: TextPreparer

    private val textProvider = mockk<TextProvider>()
    private val playerRegistry = mockk<PlayerRegistry>()
    private val textFormatter = mockk<TextFormatter>()
    private val consumer = mockk<(Player, String) -> Unit>(relaxed = true)
    private val consumerForAll = mockk<(String) -> Unit>(relaxed = true)

    @BeforeEach
    fun setUp() {
        textPreparer = TextPreparer(
                textProvider = textProvider,
                playerRegistry = playerRegistry,
                textFormatter = textFormatter
        )
    }

    @Nested
    inner class PrepareForAllTests {

        @Test
        fun givenSeveralLocalesItShouldSendFormattedText() {
            val locale1 = Locale.GERMANY
            val player1 = mockk<Player> {
                every { this@mockk.locale } returns locale1
            }
            val locale2 = Locale.FRANCE
            val player2 = mockk<Player> {
                every { this@mockk.locale } returns locale2
            }
            every { textFormatter.format(locale1, "Hi %s", "there") } returns "Hi there 1"
            every { textFormatter.format(locale2, "Hi %s", "there") } returns "Hi there 2"
            every { playerRegistry.getAll() } returns listOf(player1, player2)

            textPreparer.prepareForAllPlayers("Hi %s", arrayOf("there"), consumer, consumerForAll)

            verify {
                consumer.invoke(player1, "Hi there 1")
                consumer.invoke(player2, "Hi there 2")
            }
        }

        @Test
        fun givenSingleLocaleItShouldSendFormattedText() {
            val locale = Locale.GERMANY
            val player1 = mockk<Player> {
                every { this@mockk.locale } returns locale
            }
            val player2 = mockk<Player> {
                every { this@mockk.locale } returns locale
            }
            every { textFormatter.format(locale, "Hi %s", "there") } returns "Hi there"
            every { playerRegistry.getAll() } returns listOf(player1, player2)

            textPreparer.prepareForAllPlayers("Hi %s", arrayOf("there"), consumer, consumerForAll)

            verify { consumerForAll.invoke("Hi there") }
        }

        @Test
        fun givenSeveralLocalesItShouldSendTranslatedText() {
            val locale1 = Locale.GERMANY
            val player1 = mockk<Player> {
                every { this@mockk.locale } returns locale1
            }
            val locale2 = Locale.FRANCE
            val player2 = mockk<Player> {
                every { this@mockk.locale } returns locale2
            }
            val textKey = TextKey("test")
            every { textProvider.getText(locale1, textKey) } returns "Hallo"
            every { textProvider.getText(locale2, textKey) } returns "Bonjour"
            every { playerRegistry.getAll() } returns listOf(player1, player2)

            textPreparer.prepareForAllPlayers(textKey, consumer, consumerForAll)

            verify {
                consumer.invoke(player1, "Hallo")
                consumer.invoke(player2, "Bonjour")
            }
        }

        @Test
        fun givenSingleLocaleItShouldSendTranslatedText() {
            val locale = Locale.GERMANY
            val player1 = mockk<Player> {
                every { this@mockk.locale } returns locale
            }
            val player2 = mockk<Player> {
                every { this@mockk.locale } returns locale
            }
            val textKey = TextKey("test")
            every { textProvider.getText(locale, textKey) } returns "Hallo"
            every { playerRegistry.getAll() } returns listOf(player1, player2)

            textPreparer.prepareForAllPlayers(textKey, consumer, consumerForAll)

            verify { consumerForAll.invoke("Hallo") }
        }

        @Test
        fun givenSeveralLocalesItShouldSendFormattedTranslatedText() {
            val locale1 = Locale.GERMANY
            val player1 = mockk<Player> {
                every { this@mockk.locale } returns locale1
            }
            val locale2 = Locale.FRANCE
            val player2 = mockk<Player> {
                every { this@mockk.locale } returns locale2
            }
            val textKey = TextKey("test")
            every { textProvider.getText(locale1, textKey) } returns "Hallo %s"
            every { textProvider.getText(locale2, textKey) } returns "Bonjour %s"
            every { playerRegistry.getAll() } returns listOf(player1, player2)
            every { textFormatter.format(locale1, "Hallo %s", "SAMP") } returns "Hallo SAMP"
            every { textFormatter.format(locale2, "Bonjour %s", "SAMP") } returns "Bonjour SAMP"

            textPreparer.prepareForAllPlayers(textKey, arrayOf("SAMP"), consumer, consumerForAll)

            verify {
                consumer.invoke(player1, "Hallo SAMP")
                consumer.invoke(player2, "Bonjour SAMP")
            }
        }

        @Test
        fun givenSingleLocalesItShouldSendFormattedTranslatedText() {
            val locale = Locale.GERMANY
            val player1 = mockk<Player> {
                every { this@mockk.locale } returns locale
            }
            val player2 = mockk<Player> {
                every { this@mockk.locale } returns locale
            }
            val textKey = TextKey("test")
            every { textProvider.getText(locale, textKey) } returns "Hallo %s"
            every { playerRegistry.getAll() } returns listOf(player1, player2)
            every { textFormatter.format(locale, "Hallo %s", "SAMP") } returns "Hallo SAMP"

            textPreparer.prepareForAllPlayers(textKey, arrayOf("SAMP"), consumer, consumerForAll)

            verify { consumerForAll.invoke("Hallo SAMP") }
        }
    }

    @Nested
    inner class PrepareForPlayerTests {

        @Test
        fun shouldTranslatedText() {
            val textKey = TextKey("test")
            val locale = Locale.GERMANY
            val player = mockk<Player> {
                every { this@mockk.locale } returns locale
            }
            every { textProvider.getText(locale, textKey) } returns "Hi there"

            textPreparer.prepareForPlayer(player, textKey, consumer)

            verify { consumer.invoke(player, "Hi there") }
        }

        @Test
        fun shouldSendFormattedText() {
            val locale = Locale.GERMANY
            val player = mockk<Player> {
                every { this@mockk.locale } returns locale
            }
            every { textFormatter.format(locale, "Hi %s", "there") } returns "Hi there"

            textPreparer.prepareForPlayer(player, "Hi %s", arrayOf("there"), consumer)

            verify { consumer.invoke(player, "Hi there") }
        }

        @Test
        fun shouldSendFormattedTranslatedText() {
            val textKey = TextKey("test")
            val locale = Locale.GERMANY
            val player = mockk<Player> {
                every { this@mockk.locale } returns locale
            }
            every { textProvider.getText(locale, textKey) } returns "Hi %s"
            every { textFormatter.format(locale, "Hi %s", "there") } returns "Hi there"

            textPreparer.prepareForPlayer(player, textKey, arrayOf("there"), consumer)

            verify { consumer.invoke(player, "Hi there") }
        }
    }

    @Nested
    inner class PrepareTests {

        @Test
        fun shouldTranslatedText() {
            val textKey = TextKey("test")
            val locale1 = Locale.GERMANY
            val player1 = mockk<Player> {
                every { this@mockk.locale } returns locale1
            }
            val locale2 = Locale.FRANCE
            val player2 = mockk<Player> {
                every { this@mockk.locale } returns locale2
            }
            val player3 = mockk<Player> {
                every { this@mockk.locale } returns locale2
            }
            every { textProvider.getText(locale1, textKey) } returns "Hi there"
            every { textProvider.getText(locale2, textKey) } returns "Bonjour"
            every { playerRegistry.getAll() } returns listOf(player1, player2, player3)

            textPreparer.prepare({ it != player2 }, textKey, consumer)

            verify {
                consumer.invoke(player1, "Hi there")
                consumer.invoke(player3, "Bonjour")
            }
        }

        @Test
        fun shouldSendFormattedText() {
            val locale1 = Locale.GERMANY
            val player1 = mockk<Player> {
                every { this@mockk.locale } returns locale1
            }
            val locale2 = Locale.FRANCE
            val player2 = mockk<Player> {
                every { this@mockk.locale } returns locale2
            }
            val player3 = mockk<Player> {
                every { this@mockk.locale } returns locale2
            }
            every { textFormatter.format(locale1, "Hi %s", "there") } returns "Hi there 1"
            every { textFormatter.format(locale2, "Hi %s", "there") } returns "Hi there 2"
            every { playerRegistry.getAll() } returns listOf(player1, player2, player3)

            textPreparer.prepare({ it != player2 }, "Hi %s", arrayOf("there"), consumer)

            verify {
                consumer.invoke(player1, "Hi there 1")
                consumer.invoke(player3, "Hi there 2")
            }
        }

        @Test
        fun shouldSendFormattedTranslatedText() {
            val textKey = TextKey("test")
            val locale1 = Locale.GERMANY
            val player1 = mockk<Player> {
                every { this@mockk.locale } returns locale1
            }
            val locale2 = Locale.FRANCE
            val player2 = mockk<Player> {
                every { this@mockk.locale } returns locale2
            }
            val player3 = mockk<Player> {
                every { this@mockk.locale } returns locale2
            }
            every { textFormatter.format(locale1, "Hi %s", "SAMP") } returns "Hi SAMP"
            every { textFormatter.format(locale2, "Bonjour %s", "SAMP") } returns "Bonjour SAMP"
            every { textProvider.getText(locale1, textKey) } returns "Hi %s"
            every { textProvider.getText(locale2, textKey) } returns "Bonjour %s"
            every { playerRegistry.getAll() } returns listOf(player1, player2, player3)

            textPreparer.prepare({ it != player2 }, textKey, arrayOf("SAMP"), consumer)

            verify {
                consumer.invoke(player1, "Hi SAMP")
                consumer.invoke(player3, "Bonjour SAMP")
            }
        }
    }

}