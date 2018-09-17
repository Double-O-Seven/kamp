package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.TextKeys
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import ch.leadrian.samp.kamp.core.api.text.TextKey
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DefaultCommandErrorHandlerTest {

    private lateinit var defaultCommandErrorHandler: CommandErrorHandler

    private val player = mockk<Player>()
    private val messageSender = mockk<MessageSender>()

    @BeforeEach
    fun setUp() {
        defaultCommandErrorHandler = DefaultCommandErrorHandler(messageSender)
    }

    @Test
    fun givenNoExceptionItShouldSendInvalidCommandMessage() {
        every { messageSender.sendMessageToPlayer(any(), any(), any<TextKey>(), any<String>()) } just Runs

        val result = defaultCommandErrorHandler.handle(player, "/hi there", null)

        verify { messageSender.sendMessageToPlayer(player, Colors.RED, TextKeys.command.invalid, "/hi there") }
        assertThat(result)
                .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
    }

    @Test
    fun givenExceptionItShouldSendUnexpectedErrorMessage() {
        every { messageSender.sendMessageToPlayer(any(), any(), any<TextKey>()) } just Runs

        val result = defaultCommandErrorHandler.handle(player, "/hi there", Exception())

        verify { messageSender.sendMessageToPlayer(player, Colors.RED, TextKeys.command.unexpected.error) }
        assertThat(result)
                .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
    }

}