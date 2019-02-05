package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Checkpoint
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.CheckpointService
import ch.leadrian.samp.kamp.streamer.runtime.CheckpointStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableCheckpointHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableCheckpointReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableCheckpointHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableCheckpointReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableCheckpointStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableCheckpointStreamInReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableCheckpointStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableCheckpointStreamOutReceiverDelegate
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

internal class StreamableCheckpointImplTest {

    private lateinit var streamableCheckpoint: StreamableCheckpointImpl
    private val checkpointStreamer: CheckpointStreamer = mockk()
    private val checkpointService: CheckpointService = mockk()
    private val onStreamableCheckpointStreamInHandler: OnStreamableCheckpointStreamInHandler = mockk()
    private val onStreamableCheckpointStreamOutHandler: OnStreamableCheckpointStreamOutHandler = mockk()
    private val onPlayerEnterStreamableCheckpointHandler: OnPlayerEnterStreamableCheckpointHandler = mockk()
    private val onPlayerLeaveStreamableCheckpointHandler: OnPlayerLeaveStreamableCheckpointHandler = mockk()
    private val onStreamableCheckpointStreamInReceiver: OnStreamableCheckpointStreamInReceiverDelegate = mockk()
    private val onStreamableCheckpointStreamOutReceiver: OnStreamableCheckpointStreamOutReceiverDelegate = mockk()
    private val onPlayerEnterStreamableCheckpointReceiver: OnPlayerEnterStreamableCheckpointReceiverDelegate = mockk()
    private val onPlayerLeaveStreamableCheckpointReceiver: OnPlayerLeaveStreamableCheckpointReceiverDelegate = mockk()
    private val checkpoint = mockk<Checkpoint>()

    @BeforeEach
    fun setUp() {
        every { checkpoint.addOnPlayerEnterCheckpointListener(any()) } just Runs
        every { checkpoint.addOnPlayerLeaveCheckpointListener(any()) } just Runs
        every { checkpointService.createCheckpoint(any(), any()) } returns checkpoint
        streamableCheckpoint = StreamableCheckpointImpl(
                coordinates = vector3DOf(1f, 2f, 3f),
                size = 4f,
                interiorIds = mutableSetOf(69),
                virtualWorldIds = mutableSetOf(1337),
                priority = 123,
                streamDistance = 75f,
                checkpointStreamer = checkpointStreamer,
                checkpointService = checkpointService,
                onStreamableCheckpointStreamInHandler = onStreamableCheckpointStreamInHandler,
                onStreamableCheckpointStreamOutHandler = onStreamableCheckpointStreamOutHandler,
                onPlayerEnterStreamableCheckpointHandler = onPlayerEnterStreamableCheckpointHandler,
                onPlayerLeaveStreamableCheckpointHandler = onPlayerLeaveStreamableCheckpointHandler,
                onStreamableCheckpointStreamInReceiver = onStreamableCheckpointStreamInReceiver,
                onStreamableCheckpointStreamOutReceiver = onStreamableCheckpointStreamOutReceiver,
                onPlayerEnterStreamableCheckpointReceiver = onPlayerEnterStreamableCheckpointReceiver,
                onPlayerLeaveStreamableCheckpointReceiver = onPlayerLeaveStreamableCheckpointReceiver
        )
    }

    @Nested
    inner class PrimitiveCheckpointTests {

        @Test
        fun shouldCreateCheckpoint() {
            verify { checkpointService.createCheckpoint(vector3DOf(1f, 2f, 3f), 4f) }
        }

        @Test
        fun shouldRegisterStreamableCheckpointAsOnPlayerEnterCheckpointListener() {
            verify { checkpoint.addOnPlayerEnterCheckpointListener(streamableCheckpoint) }
        }

        @Test
        fun shouldRegisterStreamableCheckpointAsOnPlayerLeaveCheckpointListener() {
            verify { checkpoint.addOnPlayerLeaveCheckpointListener(streamableCheckpoint) }
        }
    }

    @Nested
    inner class CoordinatesTests {

        @BeforeEach
        fun setUp() {
            every { checkpointStreamer.onBoundingBoxChange(any()) } just Runs
        }

        @Test
        fun shouldSetCheckpointCoordinates() {
            every { checkpoint.coordinates = any() } just Runs

            streamableCheckpoint.coordinates = vector3DOf(11f, 22f, 33f)

            verify { checkpoint.coordinates = vector3DOf(11f, 22f, 33f) }
        }

        @Test
        fun setShouldCallStreamer() {
            every { checkpoint.coordinates = any() } just Runs

            streamableCheckpoint.coordinates = vector3DOf(11f, 22f, 33f)

            verify { checkpointStreamer.onBoundingBoxChange(streamableCheckpoint) }
        }

    }

    @Nested
    inner class SizeTests {

        @Test
        fun shouldSetSizeOnCheckpoint() {
            every { checkpoint.size = any() } just Runs

            streamableCheckpoint.size = 44f

            verify { checkpoint.size = 44f }
        }

        @Test
        fun shouldGetSizeFromCheckpoint() {
            every { checkpoint.size } returns 44f

            assertThat(streamableCheckpoint.size)
                    .isEqualTo(44f)
        }

    }

    @Nested
    inner class VisibilityTests {

        @Test
        fun isVisibleShouldReturnTrueByDefault() {
            val player = mockk<Player>()

            val isVisible = streamableCheckpoint.isVisible(player)

            assertThat(isVisible)
                    .isTrue()
        }

        @Test
        fun givenTrueConditionIsVisibleShouldReturnTrue() {
            val player = mockk<Player>()
            streamableCheckpoint.visibleWhen { p -> p == player }

            val isVisible = streamableCheckpoint.isVisible(player)

            assertThat(isVisible)
                    .isTrue()
        }

        @Test
        fun givenFalseConditionIsVisibleShouldReturnFalse() {
            val player = mockk<Player>()
            streamableCheckpoint.visibleWhen { p -> p != player }

            val isVisible = streamableCheckpoint.isVisible(player)

            assertThat(isVisible)
                    .isFalse()
        }

    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun containsShouldReturnTrueIfAndOnlyIfStreamedInCheckpointContainsPlayer(expectedContains: Boolean) {
        val player = mockk<Player>()
        every { checkpoint.contains(player) } returns expectedContains

        val contains = streamableCheckpoint.contains(player)

        assertThat(contains)
                .isEqualTo(expectedContains)
    }

    @Nested
    inner class IsStreamedInTests {

        @Test
        fun givenCheckpointIsPlayerCheckpointItShouldReturnTrue() {
            val player = mockk<Player>()
            every { player.checkpoint } returns checkpoint

            val isStreamedIn = streamableCheckpoint.isStreamedIn(player)

            assertThat(isStreamedIn)
                    .isTrue()
        }

        @Test
        fun givenCheckpointIsPlayerCheckpointIsNullItShouldReturnFalse() {
            val player = mockk<Player>()
            every { player.checkpoint } returns null

            val isStreamedIn = streamableCheckpoint.isStreamedIn(player)

            assertThat(isStreamedIn)
                    .isFalse()
        }

        @Test
        fun givenCheckpointIsPlayerCheckpointIsOtherCheckpointItShouldReturnFalse() {
            val otherCheckpoint = mockk<Checkpoint>()
            val player = mockk<Player>()
            every { player.checkpoint } returns otherCheckpoint

            val isStreamedIn = streamableCheckpoint.isStreamedIn(player)

            assertThat(isStreamedIn)
                    .isFalse()
        }

    }

    @Nested
    inner class OnStreamInTests {

        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { player.checkpoint } returns null
            every { player.checkpoint = checkpoint } just Runs
            every { onStreamableCheckpointStreamInHandler.onStreamableCheckpointStreamIn(any(), any()) } just Runs
            every { onStreamableCheckpointStreamInReceiver.onStreamableCheckpointStreamIn(any(), any()) } just Runs
            streamableCheckpoint.onStreamIn(player)
        }

        @Test
        fun shouldSetPlayerCheckpoint() {
            verify { player.checkpoint = checkpoint }
        }

        @Test
        fun givenCheckpointIsAlreadyStreamedInOnStreamInShouldThrowException() {
            clearMocks(player, onStreamableCheckpointStreamInHandler, onStreamableCheckpointStreamInReceiver)
            every { player.checkpoint } returns checkpoint

            val caughtThrowable = catchThrowable { streamableCheckpoint.onStreamIn(player) }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Streamable checkpoint is already streamed in")
        }
    }

    @Nested
    inner class OnStreamOutTests {

        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { player.checkpoint } returns null
            every { player.checkpoint = checkpoint } just Runs
            every { player.checkpoint = null } just Runs
            every { onStreamableCheckpointStreamInHandler.onStreamableCheckpointStreamIn(any(), any()) } just Runs
            every { onStreamableCheckpointStreamInReceiver.onStreamableCheckpointStreamIn(any(), any()) } just Runs
            every { onStreamableCheckpointStreamOutHandler.onStreamableCheckpointStreamOut(any(), any()) } just Runs
            every { onStreamableCheckpointStreamOutReceiver.onStreamableCheckpointStreamOut(any(), any()) } just Runs
            streamableCheckpoint.onStreamIn(player)
            every { player.checkpoint } returns checkpoint
            streamableCheckpoint.onStreamOut(player)
        }

        @Test
        fun shouldRemovePlayerCheckpoint() {
            verify { player.checkpoint = null }
        }

        @Test
        fun givenCheckpointIsAlreadyStreamedOutOnStreamOutShouldDoNothing() {
            clearMocks(player, onStreamableCheckpointStreamOutHandler, onStreamableCheckpointStreamOutReceiver)
            every { player.checkpoint } returns null

            val caughtThrowable = catchThrowable { streamableCheckpoint.onStreamOut(player) }

            assertThat(caughtThrowable)
                    .isNull()
        }
    }

    @Nested
    inner class OnPlayerEnterCheckpointTests {

        @BeforeEach
        fun setUp() {
            every {
                onPlayerEnterStreamableCheckpointReceiver.onPlayerEnterStreamableCheckpoint(
                        any(),
                        any()
                )
            } just Runs
            every { onPlayerEnterStreamableCheckpointHandler.onPlayerEnterStreamableCheckpoint(any(), any()) } just Runs
        }

        @Test
        fun shouldCallOnPlayerEnterStreamableCheckpointReceiver() {
            val player = mockk<Player>()

            streamableCheckpoint.onPlayerEnterCheckpoint(player)

            verify {
                onPlayerEnterStreamableCheckpointReceiver.onPlayerEnterStreamableCheckpoint(
                        player,
                        streamableCheckpoint
                )
            }
        }

        @Test
        fun shouldCallOnPlayerEnterStreamableCheckpointHandler() {
            val player = mockk<Player>()

            streamableCheckpoint.onPlayerEnterCheckpoint(player)

            verify {
                onPlayerEnterStreamableCheckpointHandler.onPlayerEnterStreamableCheckpoint(
                        player,
                        streamableCheckpoint
                )
            }
        }

    }

    @Nested
    inner class OnPlayerLeaveCheckpointTests {

        @BeforeEach
        fun setUp() {
            every {
                onPlayerLeaveStreamableCheckpointReceiver.onPlayerLeaveStreamableCheckpoint(
                        any(),
                        any()
                )
            } just Runs
            every { onPlayerLeaveStreamableCheckpointHandler.onPlayerLeaveStreamableCheckpoint(any(), any()) } just Runs
        }

        @Test
        fun shouldCallOnPlayerLeaveStreamableCheckpointReceiver() {
            val player = mockk<Player>()

            streamableCheckpoint.onPlayerLeaveCheckpoint(player)

            verify {
                onPlayerLeaveStreamableCheckpointReceiver.onPlayerLeaveStreamableCheckpoint(
                        player,
                        streamableCheckpoint
                )
            }
        }

        @Test
        fun shouldCallOnPlayerLeaveStreamableCheckpointHandler() {
            val player = mockk<Player>()

            streamableCheckpoint.onPlayerLeaveCheckpoint(player)

            verify {
                onPlayerLeaveStreamableCheckpointHandler.onPlayerLeaveStreamableCheckpoint(
                        player,
                        streamableCheckpoint
                )
            }
        }

    }

    @Test
    fun isDestroyedShouldInitiallyNotBeDestroyed() {
        val isDestroyed = streamableCheckpoint.isDestroyed

        assertThat(isDestroyed)
                .isFalse()
    }

    @Nested
    inner class DestroyTests {

        @BeforeEach
        fun setUp() {
            every { checkpoint.destroy() } just Runs
            streamableCheckpoint.destroy()
        }

        @Test
        fun shouldDestroyStreamedInCheckpoints() {
            verify { checkpoint.destroy() }
        }

        @Test
        fun givenDestroyWasCalledIsDestroyedShouldReturnTrue() {
            val isDestroyed = streamableCheckpoint.isDestroyed

            assertThat(isDestroyed)
                    .isTrue()
        }

    }

    @Test
    fun shouldReturnBoundingBox() {
        every { checkpoint.coordinates } returns vector3DOf(200f, 300f, 400f)

        val boundingBox = streamableCheckpoint.getBoundingBox()

        assertThat(boundingBox)
                .isEqualTo(Rect3d(125.0, 225.0, 325.0, 275.0, 375.0, 475.0))
    }

    @Nested
    inner class DistanceToTests {

        @BeforeEach
        fun setUp() {
            every { checkpoint.coordinates } returns vector3DOf(1f, 2f, 3f)
        }

        @Test
        fun shouldReturnDistanceToLocation() {
            val location = locationOf(10f, 2f, 3f, interiorId = 69, worldId = 1337)

            val distance = streamableCheckpoint.distanceTo(location)

            assertThat(distance)
                    .isCloseTo(9f, Percentage.withPercentage(0.01))
        }

        @Test
        fun givenNoInteriorIdsAndVirtualWorldIdsItShouldReturnDistanceToLocation() {
            val location = locationOf(10f, 2f, 3f, interiorId = 69, worldId = 1337)
            streamableCheckpoint.interiorIds = mutableSetOf()
            streamableCheckpoint.virtualWorldIds = mutableSetOf()

            val distance = streamableCheckpoint.distanceTo(location)

            assertThat(distance)
                    .isCloseTo(9f, Percentage.withPercentage(0.01))
        }

        @Test
        fun givenMapObjectIsNotInInteriorItShouldReturnFloatMax() {
            val location = locationOf(1f, 2f, 3f, interiorId = 1, worldId = 45)
            streamableCheckpoint.interiorIds = mutableSetOf(0)

            val distance = streamableCheckpoint.distanceTo(location)

            assertThat(distance)
                    .isEqualTo(Float.MAX_VALUE)
        }

        @Test
        fun givenMapObjectIsNotInVirtualWorldItShouldReturnFloatMax() {
            val location = locationOf(1f, 2f, 3f, interiorId = 1, worldId = 45)
            streamableCheckpoint.virtualWorldIds = mutableSetOf(0)

            val distance = streamableCheckpoint.distanceTo(location)

            assertThat(distance)
                    .isEqualTo(Float.MAX_VALUE)
        }

    }
}