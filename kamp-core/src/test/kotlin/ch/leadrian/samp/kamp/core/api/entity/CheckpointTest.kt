package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterCheckpointListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerLeaveCheckpointListener
import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class CheckpointTest {

    private lateinit var checkpoint: Checkpoint

    private val playerRegistry = mockk<PlayerRegistry>()

    @BeforeEach
    fun setUp() {
        checkpoint = Checkpoint(
                coordinates = mutableVector3DOf(x = 1f, y = 2f, z = 3f),
                size = 4f,
                playerRegistry = playerRegistry
        )
    }

    @Nested
    inner class CoordinatesTests {

        @Test
        fun shouldInitializeInitialCoordinates() {
            val coordinates = checkpoint.coordinates

            assertThat(coordinates)
                    .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
        }

        @Test
        fun shouldUpdateCheckpointForPlayerWhereItIsActive() {
            val player1 = mockk<Player> {
                every { this@mockk.checkpoint } returns mockk()
            }
            val player2 = mockk<Player> {
                every { this@mockk.checkpoint } returns this@CheckpointTest.checkpoint
                every { this@mockk.checkpoint = any() } just Runs
            }
            val player3 = mockk<Player> {
                every { this@mockk.checkpoint } returns null
            }
            every { playerRegistry.getAll() } returns listOf(player1, player2, player3)

            checkpoint.coordinates = mutableVector3DOf(x = 11f, y = 22f, z = 33f)

            verify { player2.checkpoint = checkpoint }
            verify(exactly = 0) {
                player1.checkpoint = any()
                player3.checkpoint = any()
            }
            assertThat(checkpoint.coordinates)
                    .isEqualTo(vector3DOf(x = 11f, y = 22f, z = 33f))
        }
    }

    @Nested
    inner class SizeTests {

        @Test
        fun shouldInitializeInitialSize() {
            val size = checkpoint.size

            assertThat(size)
                    .isEqualTo(4f)
        }

        @Test
        fun shouldUpdateCheckpointForPlayerWhereItIsActive() {
            val player1 = mockk<Player> {
                every { this@mockk.checkpoint } returns mockk()
            }
            val player2 = mockk<Player> {
                every { this@mockk.checkpoint } returns this@CheckpointTest.checkpoint
                every { this@mockk.checkpoint = any() } just Runs
            }
            val player3 = mockk<Player> {
                every { this@mockk.checkpoint } returns null
            }
            every { playerRegistry.getAll() } returns listOf(player1, player2, player3)

            checkpoint.size = 44f

            verify { player2.checkpoint = checkpoint }
            verify(exactly = 0) {
                player1.checkpoint = any()
                player3.checkpoint = any()
            }
            assertThat(checkpoint.size)
                    .isEqualTo(44f)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun containsShouldReturnTrueIfAndOnlyIfPlayerIsInCheckpoint(isInCheckpoint: Boolean) {
        val player = mockk<Player>()
        every { player.isInCheckpoint(checkpoint) } returns isInCheckpoint

        val containsPlayer = player in checkpoint

        assertThat(containsPlayer)
                .isEqualTo(isInCheckpoint)
    }

    @Nested
    inner class OnPlayerEnterCheckpointListenersTests {

        @Test
        fun shouldCallAddedListener() {
            val player = mockk<Player>()
            val listener = mockk<OnPlayerEnterCheckpointListener>(relaxed = true)
            checkpoint.addOnPlayerEnterCheckpointListener(listener)

            checkpoint.onEnter(player)

            verify { listener.onPlayerEnterCheckpoint(player) }
        }

        @Test
        fun shouldNotCallRemovedListener() {
            val player = mockk<Player>()
            val listener = mockk<OnPlayerEnterCheckpointListener>(relaxed = true)
            checkpoint.addOnPlayerEnterCheckpointListener(listener)
            checkpoint.removeOnPlayerEnterCheckpointListener(listener)

            checkpoint.onEnter(player)

            verify(exactly = 0) { listener.onPlayerEnterCheckpoint(any()) }
        }

        @Test
        fun shouldCallInlinedListener() {
            val player = mockk<Player> {
                every { this@mockk.checkpoint } returns this@CheckpointTest.checkpoint
            }
            val onEnter = mockk<Checkpoint.(Player) -> Unit>(relaxed = true)
            checkpoint.onEnter(onEnter)

            checkpoint.onEnter(player)

            verify { onEnter.invoke(checkpoint, player) }
        }

        @Test
        fun shouldNotCallRemovedInlinedListener() {
            val player = mockk<Player>()
            val onEnter = mockk<Checkpoint.(Player) -> Unit>(relaxed = true)
            val listener = checkpoint.onEnter(onEnter)
            checkpoint.removeOnPlayerEnterCheckpointListener(listener)

            checkpoint.onEnter(player)

            verify(exactly = 0) { onEnter.invoke(any(), any()) }
        }

    }

    @Nested
    inner class OnPlayerLeaveCheckpointListenersTests {

        @Test
        fun shouldCallAddedListener() {
            val player = mockk<Player>()
            val listener = mockk<OnPlayerLeaveCheckpointListener>(relaxed = true)
            checkpoint.addOnPlayerLeaveCheckpointListener(listener)

            checkpoint.onLeave(player)

            verify { listener.onPlayerLeaveCheckpoint(player) }
        }

        @Test
        fun shouldNotCallRemovedListener() {
            val player = mockk<Player>()
            val listener = mockk<OnPlayerLeaveCheckpointListener>(relaxed = true)
            checkpoint.addOnPlayerLeaveCheckpointListener(listener)
            checkpoint.removeOnPlayerLeaveCheckpointListener(listener)

            checkpoint.onLeave(player)

            verify(exactly = 0) { listener.onPlayerLeaveCheckpoint(any()) }
        }

        @Test
        fun shouldCallInlinedListener() {
            val player = mockk<Player> {
                every { this@mockk.checkpoint } returns this@CheckpointTest.checkpoint
            }
            val onLeave = mockk<Checkpoint.(Player) -> Unit>(relaxed = true)
            checkpoint.onLeave(onLeave)

            checkpoint.onLeave(player)

            verify { onLeave.invoke(checkpoint, player) }
        }

        @Test
        fun shouldNotCallRemovedInlinedListener() {
            val player = mockk<Player>()
            val onLeave = mockk<Checkpoint.(Player) -> Unit>(relaxed = true)
            val listener = checkpoint.onLeave(onLeave)
            checkpoint.removeOnPlayerLeaveCheckpointListener(listener)

            checkpoint.onLeave(player)

            verify(exactly = 0) { onLeave.invoke(any(), any()) }
        }

    }

    @Nested
    inner class DestroyTests {

        private val player1 = mockk<Player>()
        private val player2 = mockk<Player>()
        private val player3 = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { player1.isInCheckpoint(this@CheckpointTest.checkpoint) } returns true
            every { player1.checkpoint = any() } just Runs
            every { player1.checkpoint } returns checkpoint
            every { player2.isInCheckpoint(this@CheckpointTest.checkpoint) } returns false
            every { player3.isInCheckpoint(this@CheckpointTest.checkpoint) } returns true
            every { player3.checkpoint = any() } just Runs
            every { player3.checkpoint } returns checkpoint
            every { playerRegistry.getAll() } returns listOf(player1, player2, player3)
        }

        @Test
        fun shouldNotBeDestroyedInitially() {
            val isDestroyed = checkpoint.isDestroyed

            assertThat(isDestroyed)
                    .isFalse()
        }

        @Test
        fun shouldCallOnLeaveForPlayersInCheckpoint() {
            val onLeave = mockk<Checkpoint.(Player?) -> Unit>(relaxed = true)
            checkpoint.onLeave(onLeave)

            checkpoint.destroy()

            verify {
                onLeave.invoke(checkpoint, player1)
                onLeave.invoke(checkpoint, player3)
            }
            verify(exactly = 0) {
                onLeave.invoke(checkpoint, player2)
            }
        }

        @Test
        fun shouldSetCheckpointToNullForPlayersInCheckpoint() {
            checkpoint.destroy()

            verify {
                player1.checkpoint = null
                player3.checkpoint = null
            }
            verify(exactly = 0) {
                player2.checkpoint = null
            }
        }

        @Test
        fun shouldBeDestroyed() {
            checkpoint.destroy()

            val isDestroyed = checkpoint.isDestroyed

            assertThat(isDestroyed)
                    .isTrue()
        }


        @Test
        fun shouldNotBeDestroyedTwice() {
            val onLeave = mockk<Checkpoint.(Player?) -> Unit>(relaxed = true)
            checkpoint.onLeave(onLeave)

            checkpoint.destroy()
            checkpoint.destroy()

            verify(exactly = 1) {
                onLeave.invoke(checkpoint, player1)
                onLeave.invoke(checkpoint, player3)
                player1.checkpoint = null
                player3.checkpoint = null
            }
        }
    }

}