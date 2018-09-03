package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.api.exception.PlayerOfflineException
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PlayerTest {

    @Nested
    inner class NonLambdaTests {

        @Test
        fun givenPlayerIsOfflineItShouldThrowAnException() {
            val player = mockk<Player> {
                every { isOnline } returns false
                every { id } returns PlayerId.valueOf(1)
            }

            val caughtThrowable = catchThrowable { player.requireOnline() }

            assertThat(caughtThrowable)
                    .isInstanceOf(PlayerOfflineException::class.java)
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

    @Nested
    inner class LambdaTests {

        @Test
        fun givenPlayerIsOfflineItShouldThrowAnException() {
            val block = mockk<Player.() -> Unit>(relaxed = true)
            val player = mockk<Player> {
                every { isOnline } returns false
                every { id } returns PlayerId.valueOf(1)
            }

            val caughtThrowable = catchThrowable { player.requireOnline(block) }

            assertThat(caughtThrowable)
                    .isInstanceOf(PlayerOfflineException::class.java)
            verify { block wasNot Called }
        }

        @Test
        fun givenPlayerIsOnlineItShouldNotThrowAnException() {
            val player = mockk<Player> {
                every { isOnline } returns true
            }
            val block = mockk<Player.() -> Int> {
                every { this@mockk.invoke(player) } returns 1337
            }

            val result = player.requireOnline(block)

            assertThat(result)
                    .isEqualTo(1337)
        }
    }

}