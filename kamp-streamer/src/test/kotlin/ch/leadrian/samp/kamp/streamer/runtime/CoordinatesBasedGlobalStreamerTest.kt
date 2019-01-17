package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.streamer.runtime.entity.CoordinatesBasedGlobalStreamable
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

internal class CoordinatesBasedGlobalStreamerTest {

    private lateinit var coordinatesBasedGlobalStreamer: CoordinatesBasedGlobalStreamer<TestStreamable, Rect3d>

    private val distanceBasedGlobalStreamerFactory: DistanceBasedGlobalStreamerFactory = mockk()
    private val distanceBasedGlobalStreamer: DistanceBasedGlobalStreamer<TestStreamable> = mockk()
    private val spatialIndex: SpatialIndex3D<TestStreamable> = mockk()
    private val spatialIndexBasedStreamableContainerFactory: SpatialIndexBasedStreamableContainerFactory = mockk()
    private val spatialIndexBasedStreamableContainer: SpatialIndexBasedStreamableContainer<TestStreamable, Rect3d> = mockk()

    @BeforeEach
    fun setUp() {
        every {
            distanceBasedGlobalStreamerFactory.create<TestStreamable>(any(), any())
        } returns distanceBasedGlobalStreamer
        every {
            spatialIndexBasedStreamableContainerFactory.create(spatialIndex, TestStreamable::class)
        } returns spatialIndexBasedStreamableContainer
        coordinatesBasedGlobalStreamer = CoordinatesBasedGlobalStreamer(
                spatialIndex,
                TestStreamable::class,
                50,
                distanceBasedGlobalStreamerFactory,
                spatialIndexBasedStreamableContainerFactory
        )
    }

    @Nested
    inner class CapacityTests {

        @Test
        fun shouldSetCapacity() {
            every { distanceBasedGlobalStreamer.capacity = any() } just Runs

            coordinatesBasedGlobalStreamer.capacity = 69

            verify { distanceBasedGlobalStreamer.capacity = 69 }
        }

        @Test
        fun shouldReturnCapacity() {
            every { distanceBasedGlobalStreamer.capacity } returns 187

            val capacity = coordinatesBasedGlobalStreamer.capacity

            assertThat(capacity)
                    .isEqualTo(187)
        }

    }

    @Test
    fun shouldDelegateStream() {
        every { spatialIndexBasedStreamableContainer.onStream() } just Runs
        every { distanceBasedGlobalStreamer.stream(any()) } just Runs
        val streamLocation1 = StreamLocation(mockk(), locationOf(1f, 2f, 3f, 4, 5))
        val streamLocation2 = StreamLocation(mockk(), locationOf(6f, 7f, 8f, 9, 10))
        val streamLocations = listOf(streamLocation1, streamLocation2)

        coordinatesBasedGlobalStreamer.stream(streamLocations)

        verifyOrder {
            spatialIndexBasedStreamableContainer.onStream()
            distanceBasedGlobalStreamer.stream(streamLocations)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldDelegateAdd(addToSpatialIndex: Boolean) {
        val streamable = TestStreamable()
        every { spatialIndexBasedStreamableContainer.add(any(), any()) } just Runs

        coordinatesBasedGlobalStreamer.add(streamable, addToSpatialIndex)

        verify { spatialIndexBasedStreamableContainer.add(streamable, addToSpatialIndex) }
    }

    @Test
    fun shouldDelegateRemove() {
        val streamable = TestStreamable()
        every { spatialIndexBasedStreamableContainer.remove(any()) } just Runs

        coordinatesBasedGlobalStreamer.remove(streamable)

        verify { spatialIndexBasedStreamableContainer.remove(streamable) }
    }

    @Test
    fun shouldDelegateRemoveFromSpatialIndex() {
        val streamable = TestStreamable()
        every { spatialIndexBasedStreamableContainer.removeFromSpatialIndex(any()) } just Runs

        coordinatesBasedGlobalStreamer.removeFromSpatialIndex(streamable)

        verify { spatialIndexBasedStreamableContainer.removeFromSpatialIndex(streamable) }
    }

    @Test
    fun shouldDelegateOnBoundingBoxChange() {
        val streamable = TestStreamable()
        every { spatialIndexBasedStreamableContainer.onBoundingBoxChange(any()) } just Runs

        coordinatesBasedGlobalStreamer.onBoundingBoxChange(streamable)

        verify { spatialIndexBasedStreamableContainer.onBoundingBoxChange(streamable) }
    }

    private class TestStreamable : CoordinatesBasedGlobalStreamable<TestStreamable, Rect3d>() {

        override val priority: Int
            get() = throw UnsupportedOperationException()

        override val streamDistance: Float
            get() = throw UnsupportedOperationException()

        override fun getBoundingBox(): Rect3d = throw UnsupportedOperationException()

        override fun onDestroy() {}

        override fun distanceTo(location: Location): Float = throw UnsupportedOperationException()

        override fun onStreamIn() {}

        override fun onStreamOut() {}

        override val isStreamedIn: Boolean
            get() = throw UnsupportedOperationException()

    }

}