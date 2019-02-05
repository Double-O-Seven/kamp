package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.runtime.entity.CoordinatesBasedPlayerStreamable
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex3D
import com.conversantmedia.util.collection.geometry.Rect3d
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class CoordinatesBasedPlayerStreamerTest {

    private lateinit var coordinatesBasedPlayerStreamer: CoordinatesBasedPlayerStreamer<TestStreamable, Rect3d>

    private val distanceBasedPlayerStreamerFactory: DistanceBasedPlayerStreamerFactory = mockk()
    private val distanceBasedPlayerStreamer: DistanceBasedPlayerStreamer<TestStreamable> = mockk()
    private val spatialIndex: SpatialIndex3D<TestStreamable> = mockk()
    private val spatialIndexBasedStreamableContainerFactory: SpatialIndexBasedStreamableContainerFactory = mockk()
    private val spatialIndexBasedStreamableContainer: SpatialIndexBasedStreamableContainer<TestStreamable, Rect3d> = mockk()

    @BeforeEach
    fun setUp() {
        every {
            distanceBasedPlayerStreamerFactory.create<TestStreamable>(any(), any())
        } returns distanceBasedPlayerStreamer
        every {
            spatialIndexBasedStreamableContainerFactory.create(spatialIndex, TestStreamable::class)
        } returns spatialIndexBasedStreamableContainer
        coordinatesBasedPlayerStreamer = CoordinatesBasedPlayerStreamer(
                spatialIndex,
                TestStreamable::class,
                50,
                distanceBasedPlayerStreamerFactory,
                spatialIndexBasedStreamableContainerFactory
        )
    }

    @Nested
    inner class CapacityTests {

        @Test
        fun shouldSetCapacity() {
            every { distanceBasedPlayerStreamer.capacity = any() } just Runs

            coordinatesBasedPlayerStreamer.capacity = 69

            verify { distanceBasedPlayerStreamer.capacity = 69 }
        }

        @Test
        fun shouldReturnCapacity() {
            every { distanceBasedPlayerStreamer.capacity } returns 187

            val capacity = coordinatesBasedPlayerStreamer.capacity

            assertThat(capacity)
                    .isEqualTo(187)
        }

    }

    @Test
    fun shouldDelegateStream() {
        every { spatialIndexBasedStreamableContainer.onStream() } just Runs
        every { distanceBasedPlayerStreamer.stream(any()) } just Runs
        val streamLocation1 = StreamLocation(mockk(), locationOf(1f, 2f, 3f, 4, 5))
        val streamLocation2 = StreamLocation(mockk(), locationOf(6f, 7f, 8f, 9, 10))
        val streamLocations = listOf(streamLocation1, streamLocation2)

        coordinatesBasedPlayerStreamer.stream(streamLocations)

        verifyOrder {
            spatialIndexBasedStreamableContainer.onStream()
            distanceBasedPlayerStreamer.stream(streamLocations)
        }
    }

    @Test
    fun shouldDelegateOnPlayerDisconnect() {
        val player = mockk<Player>()
        every { distanceBasedPlayerStreamer.onPlayerDisconnect(any(), any()) } just Runs

        coordinatesBasedPlayerStreamer.onPlayerDisconnect(player, DisconnectReason.QUIT)

        verify { distanceBasedPlayerStreamer.onPlayerDisconnect(player, DisconnectReason.QUIT) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldDelegateAdd(addToSpatialIndex: Boolean) {
        val streamable = TestStreamable()
        every { spatialIndexBasedStreamableContainer.add(any(), any()) } just Runs

        coordinatesBasedPlayerStreamer.add(streamable, addToSpatialIndex)

        verify { spatialIndexBasedStreamableContainer.add(streamable, addToSpatialIndex) }
    }

    @Test
    fun shouldDelegateRemove() {
        val streamable = TestStreamable()
        every { spatialIndexBasedStreamableContainer.remove(any()) } just Runs

        coordinatesBasedPlayerStreamer.remove(streamable)

        verify { spatialIndexBasedStreamableContainer.remove(streamable) }
    }

    @Test
    fun shouldDelegateRemoveFromSpatialIndex() {
        val streamable = TestStreamable()
        every { spatialIndexBasedStreamableContainer.removeFromSpatialIndex(any()) } just Runs

        coordinatesBasedPlayerStreamer.removeFromSpatialIndex(streamable)

        verify { spatialIndexBasedStreamableContainer.removeFromSpatialIndex(streamable) }
    }

    @Test
    fun shouldDelegateOnBoundingBoxChange() {
        val streamable = TestStreamable()
        every { spatialIndexBasedStreamableContainer.onBoundingBoxChange(any()) } just Runs

        coordinatesBasedPlayerStreamer.onBoundingBoxChange(streamable)

        verify { spatialIndexBasedStreamableContainer.onBoundingBoxChange(streamable) }
    }

    private class TestStreamable : CoordinatesBasedPlayerStreamable<TestStreamable, Rect3d>() {

        override val priority: Int
            get() = throw UnsupportedOperationException()

        override val streamDistance: Float
            get() = throw UnsupportedOperationException()

        override val boundingBox: Rect3d
            get() = throw UnsupportedOperationException()

        override fun onDestroy() {}

        override fun distanceTo(location: Location): Float = throw UnsupportedOperationException()

        override fun onStreamIn(forPlayer: Player) {}

        override fun onStreamOut(forPlayer: Player) {}

        override fun isStreamedIn(forPlayer: Player): Boolean = throw UnsupportedOperationException()

        override fun isVisible(forPlayer: Player): Boolean = throw UnsupportedOperationException()

    }

}