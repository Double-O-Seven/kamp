package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.api.exception.PlayerOfflineException
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test

internal class PlayerTest {

    @Test
    fun givenPlayerIsOfflineItShouldThrowAnException() {
        val player = mockk<Player> {
            every { isOnline } returns false
            every { id } returns PlayerId.valueOf(1)
        }

        val caughtThrowable = catchThrowable { player.requireOnline() }

        assertThat(caughtThrowable)
                .isInstanceOf(PlayerOfflineException::class.java)
                .hasMessage("Player with ID 1 is already offline")
    }

    @Test
    fun givenPlayerIsOnlineItShouldNotThrowAnException() {
        val player = mockk<Player> {
            every { isOnline } returns true
        }

        val returnedPlayer = player.requireOnline()

        assertThat(returnedPlayer)
                .isSameAs(player)
    }

}