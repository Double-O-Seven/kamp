package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DefaultCommandAccessDeniedHandlerTest {

    @Test
    fun shouldReturnResultUnknownCommand() {
        val handler = DefaultCommandAccessDeniedHandler()

        val result = handler.handle(mockk(), mockk(), listOf("a"))

        assertThat(result)
                .isEqualTo(OnPlayerCommandTextListener.Result.UnknownCommand)
    }

}