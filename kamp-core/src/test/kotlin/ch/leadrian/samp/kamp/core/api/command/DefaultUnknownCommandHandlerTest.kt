package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.TextKeys
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DefaultUnknownCommandHandlerTest {

    private val player = mockk<Player>()
    private lateinit var defaultUnknownCommandHandler: DefaultUnknownCommandHandler

    private val textProvider = mockk<TextProvider>()
    private val messageSender = mockk<MessageSender>()

    @BeforeEach
    fun setUp() {
        defaultUnknownCommandHandler = DefaultUnknownCommandHandler(textProvider, messageSender)
    }

    @Test
    fun shouldSendClientMessage() {
        every { messageSender.sendMessageToPlayer(any(), any(), any<TextKey>(), any(), any()) } just Runs

        defaultUnknownCommandHandler.handle(player, "help", listOf("abc", "xyz"))

        verify {
            messageSender.sendMessageToPlayer(player, Colors.RED, TextKeys.command.unknown, "help", "abc xyz")
        }
    }

    @Test
    fun shouldReturnResultProcessed() {
        every { messageSender.sendMessageToPlayer(any(), any(), any<TextKey>(), any(), any()) } just Runs

        val result = defaultUnknownCommandHandler.handle(player, "help", listOf("abc", "xyz"))

        assertThat(result)
                .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
    }

}