package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DefaultCommandAccessDeniedHandlerTest {

    @Test
    fun shouldReturnResultUnknownCommand() {
        val method = Any::class.java.getMethod("hashCode")
        val handler = DefaultCommandAccessDeniedHandler()

        val result = handler.handle(mockk(), "test", listOf("a"), method)

        assertThat(result)
                .isEqualTo(OnPlayerCommandTextListener.Result.UnknownCommand)
    }

}