package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.RaceCheckpoint
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class RaceCheckpointCallbackListenerTest {

    private lateinit var raceCheckpointCallbackListener: RaceCheckpointCallbackListener

    private val callbackListenerManager = mockk<CallbackListenerManager>()

    @BeforeEach
    fun setUp() {
        raceCheckpointCallbackListener = RaceCheckpointCallbackListener(callbackListenerManager)
    }

    @Test
    fun shouldRegisterOnInitialize() {
        every { callbackListenerManager.register(any()) } just Runs

        raceCheckpointCallbackListener.initialize()

        verify { callbackListenerManager.register(raceCheckpointCallbackListener) }
    }

    @Nested
    inner class OnPlayerEnterRaceCheckpointTests {

        private val player = mockk<Player>()

        @Test
        fun givenPlayerHasRaceCheckpointSetItShouldCallOnEnter() {
            val raceCheckpoint = mockk<RaceCheckpoint>()
            every { player.raceCheckpoint } returns raceCheckpoint
            every { raceCheckpoint.onEnter(any<Player>()) } just Runs

            raceCheckpointCallbackListener.onPlayerEnterRaceCheckpoint(player)

            verify { raceCheckpoint.onEnter(player) }
        }

        @Test
        fun givenPlayerHasNoRaceCheckpointSetItShouldDoNothing() {
            every { player.raceCheckpoint } returns null

            val caughtThrowable = catchThrowable { raceCheckpointCallbackListener.onPlayerEnterRaceCheckpoint(player) }

            assertThat(caughtThrowable)
                    .isNull()
        }
    }

    @Nested
    inner class OnPlayerLeaveRaceCheckpointTests {

        private val player = mockk<Player>()

        @Test
        fun givenPlayerHasRaceCheckpointSetItShouldCallOnLeave() {
            val raceCheckpoint = mockk<RaceCheckpoint>()
            every { player.raceCheckpoint } returns raceCheckpoint
            every { raceCheckpoint.onLeave(any<Player>()) } just Runs

            raceCheckpointCallbackListener.onPlayerLeaveRaceCheckpoint(player)

            verify { raceCheckpoint.onLeave(player) }
        }

        @Test
        fun givenPlayerHasNoRaceCheckpointSetItShouldDoNothing() {
            every { player.raceCheckpoint } returns null

            val caughtThrowable = catchThrowable { raceCheckpointCallbackListener.onPlayerLeaveRaceCheckpoint(player) }

            assertThat(caughtThrowable)
                    .isNull()
        }
    }

}