package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterRaceCheckpointListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerLeaveRaceCheckpointListener
import ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.core.api.data.Vector3D
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

internal class RaceCheckpointTest {

    private lateinit var raceCheckpoint: RaceCheckpoint

    private val playerRegistry = mockk<PlayerRegistry>()

    @BeforeEach
    fun setUp() {
        raceCheckpoint = RaceCheckpoint(
                coordinates = mutableVector3DOf(x = 1f, y = 2f, z = 3f),
                size = 4f,
                nextCoordinates = mutableVector3DOf(5f, 6f, 7f),
                type = RaceCheckpointType.AIR_FINISH,
                playerRegistry = playerRegistry
        )
    }

    @Nested
    inner class CoordinatesTests {

        @Test
        fun shouldInitializeInitialCoordinates() {
            val coordinates = raceCheckpoint.coordinates

            assertThat(coordinates)
                    .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
        }

        @Test
        fun shouldUpdateRaceCheckpointForPlayerWhereItIsActive() {
            val player1 = mockk<Player> {
                every { this@mockk.raceCheckpoint } returns mockk()
            }
            val player2 = mockk<Player> {
                every { this@mockk.raceCheckpoint } returns this@RaceCheckpointTest.raceCheckpoint
                every { this@mockk.raceCheckpoint = any() } just Runs
            }
            val player3 = mockk<Player> {
                every { this@mockk.raceCheckpoint } returns null
            }
            every { playerRegistry.getAll() } returns listOf(player1, player2, player3)

            raceCheckpoint.coordinates = mutableVector3DOf(x = 11f, y = 22f, z = 33f)

            verify { player2.raceCheckpoint = raceCheckpoint }
            verify(exactly = 0) {
                player1.raceCheckpoint = any()
                player3.raceCheckpoint = any()
            }
            assertThat(raceCheckpoint.coordinates)
                    .isEqualTo(vector3DOf(x = 11f, y = 22f, z = 33f))
        }
    }

    @Nested
    inner class NextCoordinatesTests {

        @Test
        fun shouldInitializeInitialNextCoordinates() {
            val nextCoordinates = raceCheckpoint.nextCoordinates

            assertThat(nextCoordinates)
                    .isEqualTo(vector3DOf(x = 5f, y = 6f, z = 7f))
        }

        @Test
        fun shouldUpdateRaceCheckpointForPlayerWhereItIsActive() {
            val player1 = mockk<Player> {
                every { this@mockk.raceCheckpoint } returns mockk()
            }
            val player2 = mockk<Player> {
                every { this@mockk.raceCheckpoint } returns this@RaceCheckpointTest.raceCheckpoint
                every { this@mockk.raceCheckpoint = any() } just Runs
            }
            val player3 = mockk<Player> {
                every { this@mockk.raceCheckpoint } returns null
            }
            every { playerRegistry.getAll() } returns listOf(player1, player2, player3)

            raceCheckpoint.nextCoordinates = mutableVector3DOf(x = 11f, y = 22f, z = 33f)

            verify { player2.raceCheckpoint = raceCheckpoint }
            verify(exactly = 0) {
                player1.raceCheckpoint = any()
                player3.raceCheckpoint = any()
            }
            assertThat(raceCheckpoint.nextCoordinates)
                    .isEqualTo(vector3DOf(x = 11f, y = 22f, z = 33f))
        }

        @Test
        fun givenNullAsNextCoordinatesItShouldUpdateRaceCheckpointForPlayerWhereItIsActive() {
            val player1 = mockk<Player> {
                every { this@mockk.raceCheckpoint } returns mockk()
            }
            val player2 = mockk<Player> {
                every { this@mockk.raceCheckpoint } returns this@RaceCheckpointTest.raceCheckpoint
                every { this@mockk.raceCheckpoint = any() } just Runs
            }
            val player3 = mockk<Player> {
                every { this@mockk.raceCheckpoint } returns null
            }
            every { playerRegistry.getAll() } returns listOf(player1, player2, player3)
            val nextCoordinates: Vector3D? = null

            raceCheckpoint.nextCoordinates = nextCoordinates

            verify { player2.raceCheckpoint = raceCheckpoint }
            verify(exactly = 0) {
                player1.raceCheckpoint = any()
                player3.raceCheckpoint = any()
            }
            assertThat(raceCheckpoint.nextCoordinates)
                    .isNull()
        }
    }

    @Nested
    inner class SizeTests {

        @Test
        fun shouldInitializeInitialSize() {
            val size = raceCheckpoint.size

            assertThat(size)
                    .isEqualTo(4f)
        }

        @Test
        fun shouldUpdateRaceCheckpointForPlayerWhereItIsActive() {
            val player1 = mockk<Player> {
                every { this@mockk.raceCheckpoint } returns mockk()
            }
            val player2 = mockk<Player> {
                every { this@mockk.raceCheckpoint } returns this@RaceCheckpointTest.raceCheckpoint
                every { this@mockk.raceCheckpoint = any() } just Runs
            }
            val player3 = mockk<Player> {
                every { this@mockk.raceCheckpoint } returns null
            }
            every { playerRegistry.getAll() } returns listOf(player1, player2, player3)

            raceCheckpoint.size = 44f

            verify { player2.raceCheckpoint = raceCheckpoint }
            verify(exactly = 0) {
                player1.raceCheckpoint = any()
                player3.raceCheckpoint = any()
            }
            assertThat(raceCheckpoint.size)
                    .isEqualTo(44f)
        }
    }

    @Nested
    inner class TypeTests {

        @Test
        fun shouldInitializeInitialType() {
            val type = raceCheckpoint.type

            assertThat(type)
                    .isEqualTo(RaceCheckpointType.AIR_FINISH)
        }

        @Test
        fun shouldUpdateRaceCheckpointForPlayerWhereItIsActive() {
            val player1 = mockk<Player> {
                every { this@mockk.raceCheckpoint } returns mockk()
            }
            val player2 = mockk<Player> {
                every { this@mockk.raceCheckpoint } returns this@RaceCheckpointTest.raceCheckpoint
                every { this@mockk.raceCheckpoint = any() } just Runs
            }
            val player3 = mockk<Player> {
                every { this@mockk.raceCheckpoint } returns null
            }
            every { playerRegistry.getAll() } returns listOf(player1, player2, player3)

            raceCheckpoint.type = RaceCheckpointType.AIR_ROTATES_AND_STOPS

            verify { player2.raceCheckpoint = raceCheckpoint }
            verify(exactly = 0) {
                player1.raceCheckpoint = any()
                player3.raceCheckpoint = any()
            }
            assertThat(raceCheckpoint.type)
                    .isEqualTo(RaceCheckpointType.AIR_ROTATES_AND_STOPS)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun containsShouldReturnTrueIfAndOnlyIfPlayerIsInCheckpoint(isInRaceCheckpoint: Boolean) {
        val player = mockk<Player>()
        every { player.isInRaceCheckpoint(raceCheckpoint) } returns isInRaceCheckpoint

        val containsPlayer = player in raceCheckpoint

        assertThat(containsPlayer)
                .isEqualTo(isInRaceCheckpoint)
    }

    @Nested
    inner class OnPlayerEnterRaceCheckpointListenersTests {

        @Test
        fun shouldCallAddedListener() {
            val player = mockk<Player>()
            val listener = mockk<OnPlayerEnterRaceCheckpointListener>(relaxed = true)
            raceCheckpoint.addOnPlayerEnterRaceCheckpointListener(listener)

            raceCheckpoint.onEnter(player)

            verify { listener.onPlayerEnterRaceCheckpoint(player) }
        }

        @Test
        fun shouldNotCallRemovedListener() {
            val player = mockk<Player>()
            val listener = mockk<OnPlayerEnterRaceCheckpointListener>(relaxed = true)
            raceCheckpoint.addOnPlayerEnterRaceCheckpointListener(listener)
            raceCheckpoint.removeOnPlayerEnterRaceCheckpointListener(listener)

            raceCheckpoint.onEnter(player)

            verify(exactly = 0) { listener.onPlayerEnterRaceCheckpoint(any()) }
        }

        @Test
        fun shouldCallInlinedListener() {
            val player = mockk<Player> {
                every { this@mockk.raceCheckpoint } returns this@RaceCheckpointTest.raceCheckpoint
            }
            val onEnter = mockk<RaceCheckpoint.(Player) -> Unit>(relaxed = true)
            raceCheckpoint.onEnter(onEnter)

            raceCheckpoint.onEnter(player)

            verify { onEnter.invoke(raceCheckpoint, player) }
        }

        @Test
        fun shouldNotCallRemovedInlinedListener() {
            val player = mockk<Player>()
            val onEnter = mockk<RaceCheckpoint.(Player) -> Unit>(relaxed = true)
            val listener = raceCheckpoint.onEnter(onEnter)
            raceCheckpoint.removeOnPlayerEnterRaceCheckpointListener(listener)

            raceCheckpoint.onEnter(player)

            verify(exactly = 0) { onEnter.invoke(any(), any()) }
        }

    }

    @Nested
    inner class OnPlayerLeaveRaceCheckpointListenersTests {

        @Test
        fun shouldCallAddedListener() {
            val player = mockk<Player>()
            val listener = mockk<OnPlayerLeaveRaceCheckpointListener>(relaxed = true)
            raceCheckpoint.addOnPlayerLeaveRaceCheckpointListener(listener)

            raceCheckpoint.onLeave(player)

            verify { listener.onPlayerLeaveRaceCheckpoint(player) }
        }

        @Test
        fun shouldNotCallRemovedListener() {
            val player = mockk<Player>()
            val listener = mockk<OnPlayerLeaveRaceCheckpointListener>(relaxed = true)
            raceCheckpoint.addOnPlayerLeaveRaceCheckpointListener(listener)
            raceCheckpoint.removeOnPlayerLeaveRaceCheckpointListener(listener)

            raceCheckpoint.onLeave(player)

            verify(exactly = 0) { listener.onPlayerLeaveRaceCheckpoint(any()) }
        }

        @Test
        fun shouldCallInlinedListener() {
            val player = mockk<Player> {
                every { this@mockk.raceCheckpoint } returns this@RaceCheckpointTest.raceCheckpoint
            }
            val onLeave = mockk<RaceCheckpoint.(Player) -> Unit>(relaxed = true)
            raceCheckpoint.onLeave(onLeave)

            raceCheckpoint.onLeave(player)

            verify { onLeave.invoke(raceCheckpoint, player) }
        }

        @Test
        fun shouldNotCallRemovedInlinedListener() {
            val player = mockk<Player>()
            val onLeave = mockk<RaceCheckpoint.(Player) -> Unit>(relaxed = true)
            val listener = raceCheckpoint.onLeave(onLeave)
            raceCheckpoint.removeOnPlayerLeaveRaceCheckpointListener(listener)

            raceCheckpoint.onLeave(player)

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
            every { player1.isInRaceCheckpoint(this@RaceCheckpointTest.raceCheckpoint) } returns true
            every { player1.raceCheckpoint = any() } just Runs
            every { player1.raceCheckpoint } returns raceCheckpoint
            every { player2.isInRaceCheckpoint(this@RaceCheckpointTest.raceCheckpoint) } returns false
            every { player3.isInRaceCheckpoint(this@RaceCheckpointTest.raceCheckpoint) } returns true
            every { player3.raceCheckpoint = any() } just Runs
            every { player3.raceCheckpoint } returns raceCheckpoint
            every { playerRegistry.getAll() } returns listOf(player1, player2, player3)
        }

        @Test
        fun shouldNotBeDestroyedInitially() {
            val isDestroyed = raceCheckpoint.isDestroyed

            assertThat(isDestroyed)
                    .isFalse()
        }

        @Test
        fun shouldCallOnLeaveForPlayersInRaceCheckpoint() {
            val onLeave = mockk<RaceCheckpoint.(Player?) -> Unit>(relaxed = true)
            raceCheckpoint.onLeave(onLeave)

            raceCheckpoint.destroy()

            verify {
                onLeave.invoke(raceCheckpoint, player1)
                onLeave.invoke(raceCheckpoint, player3)
            }
            verify(exactly = 0) {
                onLeave.invoke(raceCheckpoint, player2)
            }
        }

        @Test
        fun shouldSetRaceCheckpointToNullForPlayersInRaceCheckpoint() {
            raceCheckpoint.destroy()

            verify {
                player1.raceCheckpoint = null
                player3.raceCheckpoint = null
            }
            verify(exactly = 0) {
                player2.raceCheckpoint = null
            }
        }

        @Test
        fun shouldBeDestroyed() {
            raceCheckpoint.destroy()

            val isDestroyed = raceCheckpoint.isDestroyed

            assertThat(isDestroyed)
                    .isTrue()
        }

        @Test
        fun shouldNotBeDestroyedTwice() {
            val onLeave = mockk<RaceCheckpoint.(Player?) -> Unit>(relaxed = true)
            raceCheckpoint.onLeave(onLeave)

            raceCheckpoint.destroy()
            raceCheckpoint.destroy()

            verify(exactly = 1) {
                onLeave.invoke(raceCheckpoint, player1)
                onLeave.invoke(raceCheckpoint, player3)
                player1.raceCheckpoint = null
                player3.raceCheckpoint = null
            }
        }
    }

}