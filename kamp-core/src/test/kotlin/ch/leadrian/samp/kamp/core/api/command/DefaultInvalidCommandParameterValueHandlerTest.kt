package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.TextKeys
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.Locale

internal class DefaultInvalidCommandParameterValueHandlerTest {

    @Test
    fun shouldSendMessageToPlayer() {
        val locale = Locale.GERMANY
        val player = mockk<Player> {
            every { this@mockk.locale } returns locale
        }
        val parameter1 = mockk<CommandParameterDefinition> {
            every { getName(locale) } returns "Player ID"
        }
        val parameter2 = mockk<CommandParameterDefinition> {
            every { getName(locale) } returns "Message"
        }
        val command = mockk<CommandDefinition> {
            every { name } returns "pm"
            every { parameters } returns listOf(parameter1, parameter2)
        }
        val textProvider = mockk<TextProvider> {
            every { getText(locale, TextKeys.command.usage.prefix) } returns "Usage"
        }
        val messageSender = mockk<MessageSender> {
            every { sendMessageToPlayer(any(), any(), any<String>()) } just Runs
        }
        val defaultUnknownCommandHandler = DefaultInvalidCommandParameterValueHandler(
                textProvider = textProvider,
                messageSender = messageSender
        )

        defaultUnknownCommandHandler.handle(player, command, emptyList(), null)

        verify {
            messageSender.sendMessageToPlayer(player, Colors.RED, "Usage: /pm [Player ID] [Message]")
        }
    }

}