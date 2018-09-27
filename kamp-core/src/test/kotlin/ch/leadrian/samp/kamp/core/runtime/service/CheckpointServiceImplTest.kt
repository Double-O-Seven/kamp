package ch.leadrian.samp.kamp.core.runtime.service

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.data.sphereOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Checkpoint
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.entity.factory.CheckpointFactory
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

internal class CheckpointServiceImplTest {

    private lateinit var checkpointService: CheckpointServiceImpl

    private val checkpointFactory = mockk<CheckpointFactory>()
    private val callbackListenerManager = mockk<CallbackListenerManager>()

    @BeforeEach
    fun setUp() {
        checkpointService = CheckpointServiceImpl(checkpointFactory, callbackListenerManager)
    }

    @Test
    fun shouldRegisterOnInitialize() {
        every { callbackListenerManager.register(any()) } just Runs

        checkpointService.initialize()

        verify { callbackListenerManager.register(checkpointService) }
    }

    @Nested
    inner class CreateCheckpointTests {

        private val checkpoint = mockk<Checkpoint>()

        @BeforeEach
        fun setUp() {
            every { checkpointFactory.create(any(), any()) } returns checkpoint
        }

        @Test
        fun shouldCreateCheckpointWithCoordinatesAndSize() {
            val createdCheckpoint = checkpointService.createCheckpoint(vector3DOf(x = 1f, y = 2f, z = 3f), 4f)

            verify { checkpointFactory.create(vector3DOf(x = 1f, y = 2f, z = 3f), 4f) }
            assertThat(createdCheckpoint)
                    .isEqualTo(checkpoint)
        }

        @Test
        fun shouldCreateCheckpointWithSphere() {
            val createdCheckpoint = checkpointService.createCheckpoint(sphereOf(x = 1f, y = 2f, z = 3f, radius = 4f))

            verify { checkpointFactory.create(vector3DOf(x = 1f, y = 2f, z = 3f), 4f) }
            assertThat(createdCheckpoint)
                    .isEqualTo(checkpoint)
        }
    }

    @Nested
    inner class OnPlayerEnterCheckpointTests {

        private val player = mockk<Player>()

        @Test
        fun givenPlayerHasCheckpointSetItShouldCallOnEnter() {
            val checkpoint = mockk<Checkpoint>()
            every { player.checkpoint } returns checkpoint
            every { checkpoint.onEnter(any<Player>()) } just Runs

            checkpointService.onPlayerEnterCheckpoint(player)

            verify { checkpoint.onEnter(player) }
        }

        @Test
        fun givenPlayerHasNoCheckpointSetItShouldDoNothing() {
            every { player.checkpoint } returns null

            val caughtThrowable = catchThrowable { checkpointService.onPlayerEnterCheckpoint(player) }

            assertThat(caughtThrowable)
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

            checkpointService.onPlayerLeaveCheckpoint(player)

            verify { checkpoint.onLeave(player) }
        }

        @Test
        fun givenPlayerHasNoCheckpointSetItShouldDoNothing() {
            every { player.checkpoint } returns null

            val caughtThrowable = catchThrowable { checkpointService.onPlayerLeaveCheckpoint(player) }

            assertThat(caughtThrowable)
                    .isNull()
        }
    }

}