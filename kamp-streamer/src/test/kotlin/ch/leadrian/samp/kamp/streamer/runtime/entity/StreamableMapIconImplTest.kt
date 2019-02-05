package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.core.api.constants.MapIconType
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapIcon
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapIconId
import ch.leadrian.samp.kamp.streamer.runtime.MapIconStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapIconStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapIconStreamInReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapIconStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapIconStreamOutReceiverDelegate
import com.conversantmedia.util.collection.geometry.Rect3d
import io.mockk.Called
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

internal class StreamableMapIconImplTest {

    private lateinit var streamableMapIcon: StreamableMapIconImpl
    private val playerMapIconIdAllocator: PlayerMapIconIdAllocator = mockk()
    private val mapIconStreamer: MapIconStreamer = mockk()
    private val onStreamableMapIconStreamInHandler: OnStreamableMapIconStreamInHandler = mockk()
    private val onStreamableMapIconStreamOutHandler: OnStreamableMapIconStreamOutHandler = mockk()
    private val onStreamableMapIconStreamInReceiver: OnStreamableMapIconStreamInReceiverDelegate = mockk()
    private val onStreamableMapIconStreamOutReceiver: OnStreamableMapIconStreamOutReceiverDelegate = mockk()

    @BeforeEach
    fun setUp() {
        streamableMapIcon = StreamableMapIconImpl(
                coordinates = vector3DOf(1f, 2f, 3f),
                type = MapIconType.AIR_YARD,
                color = Colors.RED,
                style = MapIconStyle.GLOBAL,
                virtualWorldIds = mutableSetOf(69),
                interiorIds = mutableSetOf(1337),
                streamDistance = 75f,
                priority = 187,
                mapIconStreamer = mapIconStreamer,
                playerMapIconIdAllocator = playerMapIconIdAllocator,
                onStreamableMapIconStreamInHandler = onStreamableMapIconStreamInHandler,
                onStreamableMapIconStreamOutHandler = onStreamableMapIconStreamOutHandler,
                onStreamableMapIconStreamInReceiver = onStreamableMapIconStreamInReceiver,
                onStreamableMapIconStreamOutReceiver = onStreamableMapIconStreamOutReceiver
        )
    }

    @Nested
    inner class CoordinatesTests {

        @BeforeEach
        fun setUp() {
            every { mapIconStreamer.onBoundingBoxChange(any()) } just Runs
        }

        @Test
        fun shouldInitializeCoordinates() {
            val coordinates = streamableMapIcon.coordinates

            assertThat(coordinates)
                    .isEqualTo(vector3DOf(1f, 2f, 3f))
        }

        @Test
        fun shouldCallOnBoundingBoxChanged() {
            streamableMapIcon.coordinates = vector3DOf(11f, 22f, 33f)

            verify { mapIconStreamer.onBoundingBoxChange(streamableMapIcon) }
        }

        @Test
        fun shouldUpdateCoordinates() {
            streamableMapIcon.coordinates = vector3DOf(11f, 22f, 33f)

            assertThat(streamableMapIcon.coordinates)
                    .isEqualTo(vector3DOf(11f, 22f, 33f))
        }

        @Test
        fun shouldUpdatePlayerMapIcon() {
            every { onStreamableMapIconStreamInReceiver.onStreamableMapIconStreamIn(any(), any()) } just Runs
            every { onStreamableMapIconStreamInHandler.onStreamableMapIconStreamIn(any(), any()) } just Runs
            val playerMapIcon = mockk<PlayerMapIcon> {
                every { coordinates = any() } just Runs
            }
            val player = mockk<Player> {
                every { createMapIcon(any(), any(), any(), any(), any()) } returns playerMapIcon
            }
            every { playerMapIconIdAllocator.allocate(any()) } returns mockk(relaxed = true)
            streamableMapIcon.onStreamIn(player)

            streamableMapIcon.coordinates = vector3DOf(11f, 22f, 33f)

            verify { playerMapIcon.coordinates = vector3DOf(11f, 22f, 33f) }
        }

    }

    @Nested
    inner class ColorTests {

        @Test
        fun shouldInitializeColor() {
            val color = streamableMapIcon.color

            assertThat(color)
                    .isEqualTo(Colors.RED)
        }

        @Test
        fun shouldUpdateColor() {
            streamableMapIcon.color = Colors.BLUE

            assertThat(streamableMapIcon.color)
                    .isEqualTo(Colors.BLUE)
        }

        @Test
        fun shouldUpdatePlayerMapIcon() {
            every { onStreamableMapIconStreamInReceiver.onStreamableMapIconStreamIn(any(), any()) } just Runs
            every { onStreamableMapIconStreamInHandler.onStreamableMapIconStreamIn(any(), any()) } just Runs
            val playerMapIcon = mockk<PlayerMapIcon> {
                every { color = any() } just Runs
            }
            val player = mockk<Player> {
                every { createMapIcon(any(), any(), any(), any(), any()) } returns playerMapIcon
            }
            every { playerMapIconIdAllocator.allocate(any()) } returns mockk(relaxed = true)
            streamableMapIcon.onStreamIn(player)

            streamableMapIcon.color = Colors.BLUE

            verify { playerMapIcon.color = Colors.BLUE }
        }

    }

    @Nested
    inner class TypeTests {

        @Test
        fun shouldInitializeType() {
            val type = streamableMapIcon.type

            assertThat(type)
                    .isEqualTo(MapIconType.AIR_YARD)
        }

        @Test
        fun shouldUpdateType() {
            streamableMapIcon.type = MapIconType.BALLAS

            assertThat(streamableMapIcon.type)
                    .isEqualTo(MapIconType.BALLAS)
        }

        @Test
        fun shouldUpdatePlayerMapIcon() {
            every { onStreamableMapIconStreamInReceiver.onStreamableMapIconStreamIn(any(), any()) } just Runs
            every { onStreamableMapIconStreamInHandler.onStreamableMapIconStreamIn(any(), any()) } just Runs
            val playerMapIcon = mockk<PlayerMapIcon> {
                every { type = any() } just Runs
            }
            val player = mockk<Player> {
                every { createMapIcon(any(), any(), any(), any(), any()) } returns playerMapIcon
            }
            every { playerMapIconIdAllocator.allocate(any()) } returns mockk(relaxed = true)
            streamableMapIcon.onStreamIn(player)

            streamableMapIcon.type = MapIconType.BALLAS

            verify { playerMapIcon.type = MapIconType.BALLAS }
        }

    }

    @Nested
    inner class StyleTests {

        @Test
        fun shouldInitializeStyle() {
            val style = streamableMapIcon.style

            assertThat(style)
                    .isEqualTo(MapIconStyle.GLOBAL)
        }

        @Test
        fun shouldUpdateStyle() {
            streamableMapIcon.style = MapIconStyle.LOCAL_CHECKPOINT

            assertThat(streamableMapIcon.style)
                    .isEqualTo(MapIconStyle.LOCAL_CHECKPOINT)
        }

        @Test
        fun shouldUpdatePlayerMapIcon() {
            every { onStreamableMapIconStreamInReceiver.onStreamableMapIconStreamIn(any(), any()) } just Runs
            every { onStreamableMapIconStreamInHandler.onStreamableMapIconStreamIn(any(), any()) } just Runs
            val playerMapIcon = mockk<PlayerMapIcon> {
                every { style = any() } just Runs
            }
            val player = mockk<Player> {
                every { createMapIcon(any(), any(), any(), any(), any()) } returns playerMapIcon
            }
            every { playerMapIconIdAllocator.allocate(any()) } returns mockk(relaxed = true)
            streamableMapIcon.onStreamIn(player)

            streamableMapIcon.style = MapIconStyle.LOCAL_CHECKPOINT

            verify { playerMapIcon.style = MapIconStyle.LOCAL_CHECKPOINT }
        }

    }

    @Nested
    inner class VisibilityTests {

        @Test
        fun isVisibleShouldReturnTrueByDefault() {
            val player = mockk<Player>()

            val isVisible = streamableMapIcon.isVisible(player)

            assertThat(isVisible)
                    .isTrue()
        }

        @Test
        fun givenTrueConditionIsVisibleShouldReturnTrue() {
            val player = mockk<Player>()
            streamableMapIcon.visibleWhen { p -> p == player }

            val isVisible = streamableMapIcon.isVisible(player)

            assertThat(isVisible)
                    .isTrue()
        }

        @Test
        fun givenFalseConditionIsVisibleShouldReturnFalse() {
            val player = mockk<Player>()
            streamableMapIcon.visibleWhen { p -> p != player }

            val isVisible = streamableMapIcon.isVisible(player)

            assertThat(isVisible)
                    .isFalse()
        }

    }

    @Nested
    inner class OnStreamInTests {

        private val playerMapIconId = PlayerMapIconId.valueOf(50)
        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { onStreamableMapIconStreamInReceiver.onStreamableMapIconStreamIn(any(), any()) } just Runs
            every { onStreamableMapIconStreamInHandler.onStreamableMapIconStreamIn(any(), any()) } just Runs
            val allocation = mockk<PlayerMapIconIdAllocator.Allocation> {
                every { this@mockk.playerMapIconId } returns this@OnStreamInTests.playerMapIconId
            }
            every { playerMapIconIdAllocator.allocate(player) } returns allocation
            val playerMapIcon = mockk<PlayerMapIcon>()
            every { player.createMapIcon(any(), any(), any(), any(), any()) } returns playerMapIcon
        }

        @Test
        fun shouldCreateMapIcon() {
            streamableMapIcon.onStreamIn(player)

            verify {
                player.createMapIcon(
                        playerMapIconId = playerMapIconId,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        type = MapIconType.AIR_YARD,
                        color = Colors.RED,
                        style = MapIconStyle.GLOBAL
                )
            }
        }

        @Test
        fun givenOnStreamInWasCalledIsStreamedInShouldReturnTrue() {
            streamableMapIcon.onStreamIn(player)

            val isStreamedIn = streamableMapIcon.isStreamedIn(player)

            assertThat(isStreamedIn)
                    .isTrue()
        }

        @Test
        fun callingOnStreamInTwiceInARowShouldThrowException() {
            streamableMapIcon.onStreamIn(player)
            clearMocks(playerMapIconIdAllocator, player)

            val caughtThrowable = catchThrowable { streamableMapIcon.onStreamIn(player) }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Streamable map icon is already streamed in")
            verify {
                playerMapIconIdAllocator wasNot Called
                player wasNot Called
            }
        }

        @Test
        fun shouldCallOnStreamableMapIconStreamInReceiver() {
            streamableMapIcon.onStreamIn(player)

            verify { onStreamableMapIconStreamInReceiver.onStreamableMapIconStreamIn(streamableMapIcon, player) }
        }

        @Test
        fun shouldCallOnStreamableMapIconStreamInHandler() {
            streamableMapIcon.onStreamIn(player)

            verify { onStreamableMapIconStreamInHandler.onStreamableMapIconStreamIn(streamableMapIcon, player) }
        }
    }

    @Nested
    inner class OnStreamOutTests {

        private val player = mockk<Player>()
        private val playerMapIcon = mockk<PlayerMapIcon>()
        private val allocation = mockk<PlayerMapIconIdAllocator.Allocation>()

        @BeforeEach
        fun setUp() {
            every { onStreamableMapIconStreamInReceiver.onStreamableMapIconStreamIn(any(), any()) } just Runs
            every { onStreamableMapIconStreamInHandler.onStreamableMapIconStreamIn(any(), any()) } just Runs
            every { onStreamableMapIconStreamOutReceiver.onStreamableMapIconStreamOut(any(), any()) } just Runs
            every { onStreamableMapIconStreamOutHandler.onStreamableMapIconStreamOut(any(), any()) } just Runs
            every { allocation.playerMapIconId } returns PlayerMapIconId.valueOf(50)
            every { allocation.release() } just Runs
            every { playerMapIconIdAllocator.allocate(player) } returns allocation
            every { player.createMapIcon(any(), any(), any(), any(), any()) } returns playerMapIcon
            every { playerMapIcon.destroy() } just Runs
            streamableMapIcon.onStreamIn(player)
        }

        @Test
        fun shouldDestroyMapIcon() {
            streamableMapIcon.onStreamOut(player)

            verify { playerMapIcon.destroy() }
        }

        @Test
        fun shouldReleaseAllocation() {
            streamableMapIcon.onStreamOut(player)

            verify { allocation.release() }
        }

        @Test
        fun givenOnStreamOutWasCalledIsStreamedInShouldReturnFalse() {
            streamableMapIcon.onStreamOut(player)

            val isStreamedIn = streamableMapIcon.isStreamedIn(player)

            assertThat(isStreamedIn)
                    .isFalse()
        }

        @Test
        fun callingOnStreamInTwiceInARowShouldThrowException() {
            streamableMapIcon.onStreamOut(player)

            val caughtThrowable = catchThrowable { streamableMapIcon.onStreamOut(player) }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Streamable map icon was not streamed in")
        }

        @Test
        fun shouldCallOnStreamableMapIconStreamOutReceiver() {
            streamableMapIcon.onStreamOut(player)

            verify { onStreamableMapIconStreamOutReceiver.onStreamableMapIconStreamOut(streamableMapIcon, player) }
        }

        @Test
        fun shouldCallOnStreamableMapIconStreamOutHandler() {
            streamableMapIcon.onStreamOut(player)

            verify { onStreamableMapIconStreamOutHandler.onStreamableMapIconStreamOut(streamableMapIcon, player) }
        }
    }

    @Test
    fun isStreamedInShouldInitiallyBeFalse() {
        val player = mockk<Player>()

        val isStreamedIn = streamableMapIcon.isStreamedIn(player)

        assertThat(isStreamedIn)
                .isFalse()
    }

    @Nested
    inner class DestroyTests {

        @Test
        fun isDestroyedShouldInitiallyBeFalse() {
            val isDestroyed = streamableMapIcon.isDestroyed

            assertThat(isDestroyed)
                    .isFalse()
        }

        @Test
        fun shouldDestroyPlayerMapIcon() {
            every { onStreamableMapIconStreamInReceiver.onStreamableMapIconStreamIn(any(), any()) } just Runs
            every { onStreamableMapIconStreamInHandler.onStreamableMapIconStreamIn(any(), any()) } just Runs
            val playerMapIcon = mockk<PlayerMapIcon> {
                every { coordinates = any() } just Runs
                every { destroy() } just Runs
            }
            val player = mockk<Player> {
                every { createMapIcon(any(), any(), any(), any(), any()) } returns playerMapIcon
            }
            every { playerMapIconIdAllocator.allocate(any()) } returns mockk(relaxed = true)
            streamableMapIcon.onStreamIn(player)

            streamableMapIcon.destroy()

            verify { playerMapIcon.destroy() }
        }

        @Test
        fun givenDestroyWasCalledIsDestroyedShouldReturnTrue() {
            streamableMapIcon.destroy()

            val isDestroyed = streamableMapIcon.isDestroyed

            assertThat(isDestroyed)
                    .isTrue()
        }

    }

    @Test
    fun onPlayerDisconnectShouldRemovePlayerMapIcon() {
        every { onStreamableMapIconStreamInReceiver.onStreamableMapIconStreamIn(any(), any()) } just Runs
        every { onStreamableMapIconStreamInHandler.onStreamableMapIconStreamIn(any(), any()) } just Runs
        val playerMapIcon = mockk<PlayerMapIcon> {
            every { coordinates = any() } just Runs
            every { destroy() } just Runs
        }
        val player = mockk<Player> {
            every { createMapIcon(any(), any(), any(), any(), any()) } returns playerMapIcon
        }
        every { playerMapIconIdAllocator.allocate(any()) } returns mockk(relaxed = true)
        streamableMapIcon.onStreamIn(player)

        streamableMapIcon.onPlayerDisconnect(player, DisconnectReason.QUIT)

        assertThat(streamableMapIcon.isStreamedIn(player))
                .isFalse()
    }

    @Test
    fun shouldReturnBoundingBox() {
        every { mapIconStreamer.onBoundingBoxChange(any()) } just Runs
        streamableMapIcon.coordinates = vector3DOf(200f, 300f, 400f)

        val boundingBox = streamableMapIcon.boundingBox

        assertThat(boundingBox)
                .isEqualTo(Rect3d(125.0, 225.0, 325.0, 275.0, 375.0, 475.0))
    }

    @Nested
    inner class DistanceToTests {

        @Test
        fun shouldReturnDistanceToLocation() {
            val location = locationOf(10f, 2f, 3f, interiorId = 1337, worldId = 69)

            val distance = streamableMapIcon.distanceTo(location)

            assertThat(distance)
                    .isCloseTo(9f, Percentage.withPercentage(0.01))
        }

        @Test
        fun givenMapObjectIsNotInInteriorItShouldReturnFloatMax() {
            val location = locationOf(1f, 2f, 3f, interiorId = 1, worldId = 45)
            streamableMapIcon.interiorIds = mutableSetOf(0)

            val distance = streamableMapIcon.distanceTo(location)

            assertThat(distance)
                    .isEqualTo(Float.MAX_VALUE)
        }

        @Test
        fun givenMapObjectIsNotInVirtualWorldItShouldReturnFloatMax() {
            val location = locationOf(1f, 2f, 3f, interiorId = 1, worldId = 45)
            streamableMapIcon.virtualWorldIds = mutableSetOf(0)

            val distance = streamableMapIcon.distanceTo(location)

            assertThat(distance)
                    .isEqualTo(Float.MAX_VALUE)
        }

    }
}