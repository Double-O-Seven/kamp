package ch.leadrian.samp.kamp.core.runtime.entity

import ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.RaceCheckpoint
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

internal class RaceCheckpointImplTest {

    private lateinit var raceCheckpoint: RaceCheckpointImpl

    private val playerRegistry = mockk<PlayerRegistry>()

    @BeforeEach
    fun setUp() {
        raceCheckpoint = RaceCheckpointImpl(
                coordinates = mutableVector3DOf(x = 1f, y = 2f, z = 3f),
                size = 4f,
                nextCoordinates = mutableVector3DOf(5f, 6f, 7f),
                type = ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType.AIR_FINISH,
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
            val player1 = mockk<PlayerImpl> {
                every { this@mockk.raceCheckpoint } returns mockk()
            }
            val player2 = mockk<PlayerImpl> {
                every { this@mockk.raceCheckpoint } returns this@RaceCheckpointImplTest.raceCheckpoint
                every { this@mockk.raceCheckpoint = any() } just Runs
            }
            val player3 = mockk<PlayerImpl> {
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
            val player1 = mockk<PlayerImpl> {
                every { this@mockk.raceCheckpoint } returns mockk()
            }
            val player2 = mockk<PlayerImpl> {
                every { this@mockk.raceCheckpoint } returns this@RaceCheckpointImplTest.raceCheckpoint
                every { this@mockk.raceCheckpoint = any() } just Runs
            }
            val player3 = mockk<PlayerImpl> {
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
            val player1 = mockk<PlayerImpl> {
                every { this@mockk.raceCheckpoint } returns mockk()
            }
            val player2 = mockk<PlayerImpl> {
                every { this@mockk.raceCheckpoint } returns this@RaceCheckpointImplTest.raceCheckpoint
                every { this@mockk.raceCheckpoint = any() } just Runs
            }
            val player3 = mockk<PlayerImpl> {
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
            val player1 = mockk<PlayerImpl> {
                every { this@mockk.raceCheckpoint } returns mockk()
            }
            val player2 = mockk<PlayerImpl> {
                every { this@mockk.raceCheckpoint } returns this@RaceCheckpointImplTest.raceCheckpoint
                every { this@mockk.raceCheckpoint = any() } just Runs
            }
            val player3 = mockk<PlayerImpl> {
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
                    .isEqualTo(ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType.AIR_FINISH)
        }

        @Test
        fun shouldUpdateRaceCheckpointForPlayerWhereItIsActive() {
            val player1 = mockk<PlayerImpl> {
                every { this@mockk.raceCheckpoint } returns mockk()
            }
            val player2 = mockk<PlayerImpl> {
                every { this@mockk.raceCheckpoint } returns this@RaceCheckpointImplTest.raceCheckpoint
                every { this@mockk.raceCheckpoint = any() } just Runs
            }
            val player3 = mockk<PlayerImpl> {
                every { this@mockk.raceCheckpoint } returns null
            }
            every { playerRegistry.getAll() } returns listOf(player1, player2, player3)

            raceCheckpoint.type = ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType.AIR_ROTATES_AND_STOPS

            verify { player2.raceCheckpoint = raceCheckpoint }
            verify(exactly = 0) {
                player1.raceCheckpoint = any()
                player3.raceCheckpoint = any()
            }
            assertThat(raceCheckpoint.type)
                    .isEqualTo(ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType.AIR_ROTATES_AND_STOPS)
        }
    }

    @Test
    fun shouldExecuteOnEnterHandlers() {
        val player = mockk<Player>()
        val onEnter = mockk<RaceCheckpoint.(Player?) -> Unit>(relaxed = true)
        raceCheckpoint.onEnter(onEnter)

        raceCheckpoint.onEnter(player)

        verify { onEnter.invoke(raceCheckpoint, player) }
    }

    @Test
    fun shouldExecuteOnLeaveHandlers() {
        val player = mockk<Player>()
        val onLeave = mockk<RaceCheckpoint.(Player?) -> Unit>(relaxed = true)
        raceCheckpoint.onLeave(onLeave)

        raceCheckpoint.onLeave(player)

        verify { onLeave.invoke(raceCheckpoint, player) }
    }

    @Nested
    inner class DestroyTests {

        private val player1 = mockk<PlayerImpl>()
        private val player2 = mockk<PlayerImpl>()
        private val player3 = mockk<PlayerImpl>()

        @BeforeEach
        fun setUp() {
            every { player1.isInRaceCheckpoint(this@RaceCheckpointImplTest.raceCheckpoint) } returns true
            every { player1.raceCheckpoint = any() } just Runs
            every { player2.isInRaceCheckpoint(this@RaceCheckpointImplTest.raceCheckpoint) } returns false
            every { player3.isInRaceCheckpoint(this@RaceCheckpointImplTest.raceCheckpoint) } returns true
            every { player3.raceCheckpoint = any() } just Runs
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