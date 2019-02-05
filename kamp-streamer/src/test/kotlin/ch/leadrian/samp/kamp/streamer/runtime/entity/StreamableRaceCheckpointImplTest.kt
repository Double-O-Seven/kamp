package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.RaceCheckpoint
import ch.leadrian.samp.kamp.core.api.service.RaceCheckpointService
import ch.leadrian.samp.kamp.streamer.runtime.RaceCheckpointStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableRaceCheckpointHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableRaceCheckpointReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableRaceCheckpointHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableRaceCheckpointReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableRaceCheckpointStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableRaceCheckpointStreamInReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableRaceCheckpointStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableRaceCheckpointStreamOutReceiverDelegate
import com.conversantmedia.util.collection.geometry.Rect3d
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.assertj.core.data.Percentage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class StreamableRaceCheckpointImplTest {

    private lateinit var streamableRaceCheckpoint: StreamableRaceCheckpointImpl
    private val raceCheckpointStreamer: RaceCheckpointStreamer = mockk()
    private val raceCheckpointService: RaceCheckpointService = mockk()
    private val onStreamableRaceCheckpointStreamInHandler: OnStreamableRaceCheckpointStreamInHandler = mockk()
    private val onStreamableRaceCheckpointStreamOutHandler: OnStreamableRaceCheckpointStreamOutHandler = mockk()
    private val onPlayerEnterStreamableRaceCheckpointHandler: OnPlayerEnterStreamableRaceCheckpointHandler = mockk()
    private val onPlayerLeaveStreamableRaceCheckpointHandler: OnPlayerLeaveStreamableRaceCheckpointHandler = mockk()
    private val onStreamableRaceCheckpointStreamInReceiver: OnStreamableRaceCheckpointStreamInReceiverDelegate = mockk()
    private val onStreamableRaceCheckpointStreamOutReceiver: OnStreamableRaceCheckpointStreamOutReceiverDelegate = mockk()
    private val onPlayerEnterStreamableRaceCheckpointReceiver: OnPlayerEnterStreamableRaceCheckpointReceiverDelegate = mockk()
    private val onPlayerLeaveStreamableRaceCheckpointReceiver: OnPlayerLeaveStreamableRaceCheckpointReceiverDelegate = mockk()
    private val raceCheckpoint = mockk<RaceCheckpoint>()

    @BeforeEach
    fun setUp() {
        every { raceCheckpoint.addOnPlayerEnterRaceCheckpointListener(any()) } just Runs
        every { raceCheckpoint.addOnPlayerLeaveRaceCheckpointListener(any()) } just Runs
        every { raceCheckpointService.createRaceCheckpoint(any(), any(), any(), any()) } returns raceCheckpoint
        streamableRaceCheckpoint = StreamableRaceCheckpointImpl(
                coordinates = vector3DOf(1f, 2f, 3f),
                size = 4f,
                type = RaceCheckpointType.AIR_FINISH,
                nextCoordinates = vector3DOf(1001f, 2002f, 3003f),
                interiorIds = mutableSetOf(69),
                virtualWorldIds = mutableSetOf(1337),
                priority = 123,
                streamDistance = 75f,
                raceCheckpointStreamer = raceCheckpointStreamer,
                raceCheckpointService = raceCheckpointService,
                onStreamableRaceCheckpointStreamInHandler = onStreamableRaceCheckpointStreamInHandler,
                onStreamableRaceCheckpointStreamOutHandler = onStreamableRaceCheckpointStreamOutHandler,
                onPlayerEnterStreamableRaceCheckpointHandler = onPlayerEnterStreamableRaceCheckpointHandler,
                onPlayerLeaveStreamableRaceCheckpointHandler = onPlayerLeaveStreamableRaceCheckpointHandler,
                onStreamableRaceCheckpointStreamInReceiver = onStreamableRaceCheckpointStreamInReceiver,
                onStreamableRaceCheckpointStreamOutReceiver = onStreamableRaceCheckpointStreamOutReceiver,
                onPlayerEnterStreamableRaceCheckpointReceiver = onPlayerEnterStreamableRaceCheckpointReceiver,
                onPlayerLeaveStreamableRaceCheckpointReceiver = onPlayerLeaveStreamableRaceCheckpointReceiver
        )
    }

    @Nested
    inner class PrimitiveRaceCheckpointTests {

        @Test
        fun shouldCreateRaceCheckpoint() {
            verify {
                raceCheckpointService.createRaceCheckpoint(
                        coordinates = vector3DOf(1f, 2f, 3f),
                        size = 4f,
                        type = RaceCheckpointType.AIR_FINISH,
                        nextCoordinates = vector3DOf(1001f, 2002f, 3003f)
                )
            }
        }

        @Test
        fun shouldRegisterStreamableRaceCheckpointAsOnPlayerEnterRaceCheckpointListener() {
            verify { raceCheckpoint.addOnPlayerEnterRaceCheckpointListener(streamableRaceCheckpoint) }
        }

        @Test
        fun shouldRegisterStreamableRaceCheckpointAsOnPlayerLeaveRaceCheckpointListener() {
            verify { raceCheckpoint.addOnPlayerLeaveRaceCheckpointListener(streamableRaceCheckpoint) }
        }
    }

    @Nested
    inner class CoordinatesTests {

        @BeforeEach
        fun setUp() {
            every { raceCheckpointStreamer.onBoundingBoxChange(any()) } just Runs
        }

        @Test
        fun shouldSetRaceCheckpointCoordinates() {
            every { raceCheckpoint.coordinates = any() } just Runs

            streamableRaceCheckpoint.coordinates = vector3DOf(11f, 22f, 33f)

            verify { raceCheckpoint.coordinates = vector3DOf(11f, 22f, 33f) }
        }

        @Test
        fun setShouldCallStreamer() {
            every { raceCheckpoint.coordinates = any() } just Runs

            streamableRaceCheckpoint.coordinates = vector3DOf(11f, 22f, 33f)

            verify { raceCheckpointStreamer.onBoundingBoxChange(streamableRaceCheckpoint) }
        }

        @Test
        fun shouldGetCoordinatesFromRaceCheckpoint() {
            every { raceCheckpoint.coordinates } returns vector3DOf(11f, 22f, 33f)

            val coordinates = streamableRaceCheckpoint.coordinates

            assertThat(coordinates)
                    .isEqualTo(vector3DOf(11f, 22f, 33f))
        }

    }

    @Nested
    inner class NextCoordinatesTests {

        @Test
        fun shouldSetRaceCheckpointNextCoordinates() {
            every { raceCheckpoint.nextCoordinates = any() } just Runs

            streamableRaceCheckpoint.nextCoordinates = vector3DOf(11f, 22f, 33f)

            verify { raceCheckpoint.nextCoordinates = vector3DOf(11f, 22f, 33f) }
        }

        @Test
        fun shouldGetNextCoordinatesFromRaceCheckpoint() {
            every { raceCheckpoint.nextCoordinates } returns vector3DOf(11f, 22f, 33f)

            val nextCoordinates = streamableRaceCheckpoint.nextCoordinates

            assertThat(nextCoordinates)
                    .isEqualTo(vector3DOf(11f, 22f, 33f))
        }

    }

    @Nested
    inner class SizeTests {

        @Test
        fun shouldSetSizeOnRaceCheckpoint() {
            every { raceCheckpoint.size = any() } just Runs

            streamableRaceCheckpoint.size = 44f

            verify { raceCheckpoint.size = 44f }
        }

        @Test
        fun shouldGetSizeFromRaceCheckpoint() {
            every { raceCheckpoint.size } returns 44f

            assertThat(streamableRaceCheckpoint.size)
                    .isEqualTo(44f)
        }

    }

    @Nested
    inner class TypeTests {

        @Test
        fun shouldSetTypeOnRaceCheckpoint() {
            every { raceCheckpoint.type = any() } just Runs

            streamableRaceCheckpoint.type = RaceCheckpointType.AIR_FINISH

            verify { raceCheckpoint.type = RaceCheckpointType.AIR_FINISH }
        }

        @Test
        fun shouldGetTypeFromRaceCheckpoint() {
            every { raceCheckpoint.type } returns RaceCheckpointType.AIR_FINISH

            assertThat(streamableRaceCheckpoint.type)
                    .isEqualTo(RaceCheckpointType.AIR_FINISH)
        }

    }

    @Nested
    inner class VisibilityTests {

        @Test
        fun isVisibleShouldReturnTrueByDefault() {
            val player = mockk<Player>()

            val isVisible = streamableRaceCheckpoint.isVisible(player)

            assertThat(isVisible)
                    .isTrue()
        }

        @Test
        fun givenTrueConditionIsVisibleShouldReturnTrue() {
            val player = mockk<Player>()
            streamableRaceCheckpoint.visibleWhen { p -> p == player }

            val isVisible = streamableRaceCheckpoint.isVisible(player)

            assertThat(isVisible)
                    .isTrue()
        }

        @Test
        fun givenFalseConditionIsVisibleShouldReturnFalse() {
            val player = mockk<Player>()
            streamableRaceCheckpoint.visibleWhen { p -> p != player }

            val isVisible = streamableRaceCheckpoint.isVisible(player)

            assertThat(isVisible)
                    .isFalse()
        }

    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun containsShouldReturnTrueIfAndOnlyIfStreamedInRaceCheckpointContainsPlayer(expectedContains: Boolean) {
        val player = mockk<Player>()
        every { raceCheckpoint.contains(player) } returns expectedContains

        val contains = streamableRaceCheckpoint.contains(player)

        assertThat(contains)
                .isEqualTo(expectedContains)
    }

    @Nested
    inner class IsStreamedInTests {

        @Test
        fun givenRaceCheckpointIsPlayerRaceCheckpointItShouldReturnTrue() {
            val player = mockk<Player>()
            every { player.raceCheckpoint } returns raceCheckpoint

            val isStreamedIn = streamableRaceCheckpoint.isStreamedIn(player)

            assertThat(isStreamedIn)
                    .isTrue()
        }

        @Test
        fun givenRaceCheckpointIsPlayerRaceCheckpointIsNullItShouldReturnFalse() {
            val player = mockk<Player>()
            every { player.raceCheckpoint } returns null

            val isStreamedIn = streamableRaceCheckpoint.isStreamedIn(player)

            assertThat(isStreamedIn)
                    .isFalse()
        }

        @Test
        fun givenRaceCheckpointIsPlayerRaceCheckpointIsOtherRaceCheckpointItShouldReturnFalse() {
            val otherRaceCheckpoint = mockk<RaceCheckpoint>()
            val player = mockk<Player>()
            every { player.raceCheckpoint } returns otherRaceCheckpoint

            val isStreamedIn = streamableRaceCheckpoint.isStreamedIn(player)

            assertThat(isStreamedIn)
                    .isFalse()
        }

    }

    @Nested
    inner class OnStreamInTests {

        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { player.raceCheckpoint } returns null
            every { player.raceCheckpoint = raceCheckpoint } just Runs
            every {
                onStreamableRaceCheckpointStreamInHandler.onStreamableRaceCheckpointStreamIn(
                        any(),
                        any()
                )
            } just Runs
            every {
                onStreamableRaceCheckpointStreamInReceiver.onStreamableRaceCheckpointStreamIn(
                        any(),
                        any()
                )
            } just Runs
            streamableRaceCheckpoint.onStreamIn(player)
        }

        @Test
        fun shouldSetPlayerRaceCheckpoint() {
            verify { player.raceCheckpoint = raceCheckpoint }
        }

        @Test
        fun givenRaceCheckpointIsAlreadyStreamedInOnStreamInShouldThrowException() {
            clearMocks(player, onStreamableRaceCheckpointStreamInHandler, onStreamableRaceCheckpointStreamInReceiver)
            every { player.raceCheckpoint } returns raceCheckpoint

            val caughtThrowable = catchThrowable { streamableRaceCheckpoint.onStreamIn(player) }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Streamable raceCheckpoint is already streamed in")
        }
    }

    @Nested
    inner class OnStreamOutTests {

        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { player.raceCheckpoint } returns null
            every { player.raceCheckpoint = raceCheckpoint } just Runs
            every { player.raceCheckpoint = null } just Runs
            every {
                onStreamableRaceCheckpointStreamInHandler.onStreamableRaceCheckpointStreamIn(
                        any(),
                        any()
                )
            } just Runs
            every {
                onStreamableRaceCheckpointStreamInReceiver.onStreamableRaceCheckpointStreamIn(
                        any(),
                        any()
                )
            } just Runs
            every {
                onStreamableRaceCheckpointStreamOutHandler.onStreamableRaceCheckpointStreamOut(
                        any(),
                        any()
                )
            } just Runs
            every {
                onStreamableRaceCheckpointStreamOutReceiver.onStreamableRaceCheckpointStreamOut(
                        any(),
                        any()
                )
            } just Runs
            streamableRaceCheckpoint.onStreamIn(player)
            every { player.raceCheckpoint } returns raceCheckpoint
            streamableRaceCheckpoint.onStreamOut(player)
        }

        @Test
        fun shouldRemovePlayerRaceCheckpoint() {
            verify { player.raceCheckpoint = null }
        }

        @Test
        fun givenRaceCheckpointIsAlreadyStreamedOutOnStreamOutShouldDoNothing() {
            clearMocks(player, onStreamableRaceCheckpointStreamOutHandler, onStreamableRaceCheckpointStreamOutReceiver)
            every { player.raceCheckpoint } returns null

            val caughtThrowable = catchThrowable { streamableRaceCheckpoint.onStreamOut(player) }

            assertThat(caughtThrowable)
                    .isNull()
        }
    }

    @Nested
    inner class OnPlayerEnterRaceCheckpointTests {

        @BeforeEach
        fun setUp() {
            every {
                onPlayerEnterStreamableRaceCheckpointReceiver.onPlayerEnterStreamableRaceCheckpoint(
                        any(),
                        any()
                )
            } just Runs
            every {
                onPlayerEnterStreamableRaceCheckpointHandler.onPlayerEnterStreamableRaceCheckpoint(
                        any(),
                        any()
                )
            } just Runs
        }

        @Test
        fun shouldCallOnPlayerEnterStreamableRaceCheckpointReceiver() {
            val player = mockk<Player>()

            streamableRaceCheckpoint.onPlayerEnterRaceCheckpoint(player)

            verify {
                onPlayerEnterStreamableRaceCheckpointReceiver.onPlayerEnterStreamableRaceCheckpoint(
                        player,
                        streamableRaceCheckpoint
                )
            }
        }

        @Test
        fun shouldCallOnPlayerEnterStreamableRaceCheckpointHandler() {
            val player = mockk<Player>()

            streamableRaceCheckpoint.onPlayerEnterRaceCheckpoint(player)

            verify {
                onPlayerEnterStreamableRaceCheckpointHandler.onPlayerEnterStreamableRaceCheckpoint(
                        player,
                        streamableRaceCheckpoint
                )
            }
        }

    }

    @Nested
    inner class OnPlayerLeaveRaceCheckpointTests {

        @BeforeEach
        fun setUp() {
            every {
                onPlayerLeaveStreamableRaceCheckpointReceiver.onPlayerLeaveStreamableRaceCheckpoint(
                        any(),
                        any()
                )
            } just Runs
            every {
                onPlayerLeaveStreamableRaceCheckpointHandler.onPlayerLeaveStreamableRaceCheckpoint(
                        any(),
                        any()
                )
            } just Runs
        }

        @Test
        fun shouldCallOnPlayerLeaveStreamableRaceCheckpointReceiver() {
            val player = mockk<Player>()

            streamableRaceCheckpoint.onPlayerLeaveRaceCheckpoint(player)

            verify {
                onPlayerLeaveStreamableRaceCheckpointReceiver.onPlayerLeaveStreamableRaceCheckpoint(
                        player,
                        streamableRaceCheckpoint
                )
            }
        }

        @Test
        fun shouldCallOnPlayerLeaveStreamableRaceCheckpointHandler() {
            val player = mockk<Player>()

            streamableRaceCheckpoint.onPlayerLeaveRaceCheckpoint(player)

            verify {
                onPlayerLeaveStreamableRaceCheckpointHandler.onPlayerLeaveStreamableRaceCheckpoint(
                        player,
                        streamableRaceCheckpoint
                )
            }
        }

    }

    @Test
    fun isDestroyedShouldInitiallyNotBeDestroyed() {
        val isDestroyed = streamableRaceCheckpoint.isDestroyed

        assertThat(isDestroyed)
                .isFalse()
    }

    @Nested
    inner class DestroyTests {

        @BeforeEach
        fun setUp() {
            every { raceCheckpoint.destroy() } just Runs
            streamableRaceCheckpoint.destroy()
        }

        @Test
        fun shouldDestroyStreamedInRaceCheckpoints() {
            verify { raceCheckpoint.destroy() }
        }

        @Test
        fun givenDestroyWasCalledIsDestroyedShouldReturnTrue() {
            val isDestroyed = streamableRaceCheckpoint.isDestroyed

            assertThat(isDestroyed)
                    .isTrue()
        }

    }

    @Test
    fun shouldReturnBoundingBox() {
        every { raceCheckpoint.coordinates } returns vector3DOf(200f, 300f, 400f)

        val boundingBox = streamableRaceCheckpoint.getBoundingBox()

        assertThat(boundingBox)
                .isEqualTo(Rect3d(125.0, 225.0, 325.0, 275.0, 375.0, 475.0))
    }

    @Nested
    inner class DistanceToTests {

        @BeforeEach
        fun setUp() {
            every { raceCheckpoint.coordinates } returns vector3DOf(1f, 2f, 3f)
        }

        @Test
        fun shouldReturnDistanceToLocation() {
            val location = locationOf(10f, 2f, 3f, interiorId = 69, worldId = 1337)

            val distance = streamableRaceCheckpoint.distanceTo(location)

            assertThat(distance)
                    .isCloseTo(9f, Percentage.withPercentage(0.01))
        }

        @Test
        fun givenNoInteriorIdsAndVirtualWorldIdsItShouldReturnDistanceToLocation() {
            val location = locationOf(10f, 2f, 3f, interiorId = 69, worldId = 1337)
            streamableRaceCheckpoint.interiorIds = mutableSetOf()
            streamableRaceCheckpoint.virtualWorldIds = mutableSetOf()

            val distance = streamableRaceCheckpoint.distanceTo(location)

            assertThat(distance)
                    .isCloseTo(9f, Percentage.withPercentage(0.01))
        }

        @Test
        fun givenMapObjectIsNotInInteriorItShouldReturnFloatMax() {
            val location = locationOf(1f, 2f, 3f, interiorId = 1, worldId = 45)
            streamableRaceCheckpoint.interiorIds = mutableSetOf(0)

            val distance = streamableRaceCheckpoint.distanceTo(location)

            assertThat(distance)
                    .isEqualTo(Float.MAX_VALUE)
        }

        @Test
        fun givenMapObjectIsNotInVirtualWorldItShouldReturnFloatMax() {
            val location = locationOf(1f, 2f, 3f, interiorId = 1, worldId = 45)
            streamableRaceCheckpoint.virtualWorldIds = mutableSetOf(0)

            val distance = streamableRaceCheckpoint.distanceTo(location)

            assertThat(distance)
                    .isEqualTo(Float.MAX_VALUE)
        }

    }
}