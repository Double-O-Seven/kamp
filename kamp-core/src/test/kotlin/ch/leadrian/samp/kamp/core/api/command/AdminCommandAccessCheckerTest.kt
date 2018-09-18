package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class AdminCommandAccessCheckerTest {

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldReturnExpectedValue(expectedResult: Boolean) {
        val player = mockk<Player> {
            every { isAdmin } returns expectedResult
        }
        val adminCommandAccessChecker = AdminCommandAccessChecker()

        val result = adminCommandAccessChecker.hasAccess(player, mockk(), emptyList())

        assertThat(result)
                .isEqualTo(expectedResult)
    }

}