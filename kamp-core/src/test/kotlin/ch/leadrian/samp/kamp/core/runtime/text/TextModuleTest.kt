package ch.leadrian.samp.kamp.core.runtime.text

import ch.leadrian.samp.kamp.core.api.text.GameTextSender
import ch.leadrian.samp.kamp.core.api.text.MessageFormatter
import ch.leadrian.samp.kamp.core.api.text.MessagePreparer
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import ch.leadrian.samp.kamp.core.api.text.PlayerMessageSender
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextPreparer
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.api.util.getInstance
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class TextModuleTest {

    @Test
    fun shouldCreateInjector() {
        val caughtThrowable = catchThrowable {
            Guice.createInjector(
                    TestModule(),
                    TextModule()
            )
        }

        assertThat(caughtThrowable)
                .isNull()
    }

    @Nested
    inner class InjectionTests {

        private lateinit var injector: Injector

        @BeforeEach
        fun setUp() {
            injector = Guice.createInjector(TextModule(), TestModule())
        }

        @Test
        fun shouldInjectGameTextSender() {
            val gameTextSender = injector.getInstance<GameTextSender>()

            assertThat(gameTextSender)
                    .isNotNull
        }

        @Test
        fun shouldInjectMessageFormatter() {
            val messageFormatter = injector.getInstance<MessageFormatter>()

            assertThat(messageFormatter)
                    .isNotNull
        }

        @Test
        fun shouldInjectMessagePreparer() {
            val messagePreparer = injector.getInstance<MessagePreparer>()

            assertThat(messagePreparer)
                    .isNotNull
        }

        @Test
        fun shouldInjectMessageSender() {
            val messageSender = injector.getInstance<MessageSender>()

            assertThat(messageSender)
                    .isNotNull
        }

        @Test
        fun shouldInjectPlayerMessageSender() {
            val playerMessageSender = injector.getInstance<PlayerMessageSender>()

            assertThat(playerMessageSender)
                    .isNotNull
        }

        @Test
        fun shouldInjectTextFormatter() {
            val textFormatter = injector.getInstance<TextFormatter>()

            assertThat(textFormatter)
                    .isNotNull
        }

        @Test
        fun shouldInjectTextPreparer() {
            val textPreparer = injector.getInstance<TextPreparer>()

            assertThat(textPreparer)
                    .isNotNull
        }

        @Test
        fun shouldInjectTextProvider() {
            val textProvider = injector.getInstance<TextProvider>()

            assertThat(textProvider)
                    .isNotNull
        }
    }

    private class TestModule : AbstractModule() {

        override fun configure() {
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { getMaxPlayers() } returns 50
            }
            bind(SAMPNativeFunctionExecutor::class.java).toInstance(nativeFunctionExecutor)
        }

    }
}