package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.entity.Checkpoint
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CheckpointCallbackListenerTest {

    private lateinit var checkpointCallbackListener: CheckpointCallbackListener

    private val callbackListenerManager = mockk<CallbackListenerManager>()

    @BeforeEach
    fun setUp() {
        checkpointCallbackListener = CheckpointCallbackListener(callbackListenerManager)
    }

    @Test
    fun shouldRegisterOnInitialize() {
        every { callbackListenerManager.register(any()) } just Runs

        checkpointCallbackListener.initialize()

        verify { callbackListenerManager.register(checkpointCallbackListener) }
    }

    @Nested
    inner class OnPlayerEnterCheckpointTests {

        private val player = mockk<Player>()

        @Test
        fun givenPlayerHasCheckpointSetItShouldCallOnEnter() {
            val checkpoint = mockk<Checkpoint>()
            every { player.checkpoint } returns checkpoint
            every { checkpoint.onEnter(any<Player>()) } just Runs

            checkpointCallbackListener.onPlayerEnterCheckpoint(player)

            verify { checkpoint.onEnter(player) }
        }

        @Test
        fun givenPlayerHasNoCheckpointSetItShouldDoNothing() {
            every { player.checkpoint } returns null

            val caughtThrowable = Assertions.catchThrowable { checkpointCallbackListener.onPlayerEnterCheckpoint(player) }

            Assertions.assertThat(caughtThrowable)
                    .isNull()
        }
    }

    @Nested
    inner class OnPlayerLeaveCheckpointTests {

        private val player = mockk<Player>()

        @Test
        fun givenPlayerHasCheckpointSetItShouldCallOnLeave() {
            val checkpoint = mockk<Checkpoint>()
            every { player.checkpoint } returns checkpoint
            every { checkpoint.onLeave(any<Player>()) } just Runs

            checkpointCallbackListener.onPlayerLeaveCheckpoint(player)

            verify { checkpoint.onLeave(player) }
        }

        @Test
        fun givenPlayerHasNoCheckpointSetItShouldDoNothing() {
            every { player.checkpoint } returns null

            val caughtThrowable = Assertions.catchThrowable { checkpointCallbackListener.onPlayerLeaveCheckpoint(player) }

            Assertions.assertThat(caughtThrowable)
                    .isNull()
        }
    }

}