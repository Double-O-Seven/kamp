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
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class CoordinatesBasedPlayerStreamerTest {

    private lateinit var coordinatesBasedPlayerStreamer: CoordinatesBasedPlayerStreamer<TestStreamable, Rect3d>

    private val distanceBasedPlayerStreamerFactory = mockk<DistanceBasedPlayerStreamerFactory>()
    private val distanceBasedPlayerStreamer = mockk<DistanceBasedPlayerStreamer<TestStreamable>>()
    private val spatialIndex = mockk<SpatialIndex3D<TestStreamable>>()

    @BeforeEach
    fun setUp() {
        every {
            distanceBasedPlayerStreamerFactory.create<TestStreamable>(any(), any())
        } returns distanceBasedPlayerStreamer
        coordinatesBasedPlayerStreamer = CoordinatesBasedPlayerStreamer(
                spatialIndex,
                TestStreamable::class,
                50,
                distanceBasedPlayerStreamerFactory
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
        every { distanceBasedPlayerStreamer.stream(any()) } just Runs
        val streamLocation1 = StreamLocation(mockk(), locationOf(1f, 2f, 3f, 4, 5))
        val streamLocation2 = StreamLocation(mockk(), locationOf(6f, 7f, 8f, 9, 10))
        val streamLocations = listOf(streamLocation1, streamLocation2)

        coordinatesBasedPlayerStreamer.stream(streamLocations)

        verify { distanceBasedPlayerStreamer.stream(streamLocations) }
    }

    @Test
    fun shouldDelegateOnPlayerDisconnect() {
        val player = mockk<Player>()
        every { distanceBasedPlayerStreamer.onPlayerDisconnect(any(), any()) } just Runs

        coordinatesBasedPlayerStreamer.onPlayerDisconnect(player, DisconnectReason.QUIT)

        verify { distanceBasedPlayerStreamer.onPlayerDisconnect(player, DisconnectReason.QUIT) }
    }

    @Nested
    inner class AddTests {

        @BeforeEach
        fun setUp() {
            every { distanceBasedPlayerStreamer.beforeStream(any()) } answers {
                firstArg<() -> Unit>().invoke()
            }
            every { spatialIndex.add(any()) } just Runs
        }

        @Test
        fun shouldAddStreamableToSpatialIndex() {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
            }

            coordinatesBasedPlayerStreamer.add(streamable)

            verify { spatialIndex.add(streamable) }
        }

        @Test
        fun shouldNotAddNonIndexedStreamableToSpatialIndex() {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
            }

            coordinatesBasedPlayerStreamer.add(streamable, addToSpatialIndex = false)

            verify(exactly = 0) { spatialIndex.add(any()) }
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun shouldAddStreamerAsOnDestroyListener(addToSpatialIndex: Boolean) {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
            }

            coordinatesBasedPlayerStreamer.add(streamable, addToSpatialIndex)

            verify { streamable.addOnDestroyListener(coordinatesBasedPlayerStreamer) }
        }

    }

    @Nested
    inner class RemoveTests {

        @BeforeEach
        fun setUp() {
            every { distanceBasedPlayerStreamer.beforeStream(any()) } answers {
                firstArg<() -> Unit>().invoke()
            }
            every { spatialIndex.add(any()) } just Runs
            every { spatialIndex.remove(any()) } just Runs
        }

        @Test
        fun shouldRemoveStreamableFromSpatialIndex() {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
                every { removeOnDestroyListener(any()) } just Runs
            }
            coordinatesBasedPlayerStreamer.add(streamable)

            coordinatesBasedPlayerStreamer.remove(streamable)

            verify { spatialIndex.remove(streamable) }
        }

        @Test
        fun shouldNotRemoveNonIndexedStreamableFromSpatialIndex() {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
                every { removeOnDestroyListener(any()) } just Runs
            }
            coordinatesBasedPlayerStreamer.add(streamable, addToSpatialIndex = false)

            coordinatesBasedPlayerStreamer.remove(streamable)

            verify(exactly = 0) { spatialIndex.remove(any()) }
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun shouldRemoveStreamerAsOnDestroyListener(addToSpatialIndex: Boolean) {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
                every { removeOnDestroyListener(any()) } just Runs
            }
            coordinatesBasedPlayerStreamer.add(streamable, addToSpatialIndex)

            coordinatesBasedPlayerStreamer.remove(streamable)

            verify { streamable.removeOnDestroyListener(coordinatesBasedPlayerStreamer) }
        }

    }

    @Nested
    inner class RemoveFromSpatialIndexTests {

        @BeforeEach
        fun setUp() {
            every { distanceBasedPlayerStreamer.beforeStream(any()) } answers {
                firstArg<() -> Unit>().invoke()
            }
            every { spatialIndex.add(any()) } just Runs
        }

        @Test
        fun shouldRemoveIndexedStreamableFromSpatialIndex() {
            every { spatialIndex.remove(any()) } just Runs
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
                every { removeOnDestroyListener(any()) } just Runs
            }
            coordinatesBasedPlayerStreamer.add(streamable)

            coordinatesBasedPlayerStreamer.removeFromSpatialIndex(streamable)

            verify { spatialIndex.remove(streamable) }
        }

        @Test
        fun shouldNotRemoveNonIndexedStreamableFromSpatialIndex() {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
                every { removeOnDestroyListener(any()) } just Runs
            }
            coordinatesBasedPlayerStreamer.add(streamable, addToSpatialIndex = false)

            coordinatesBasedPlayerStreamer.removeFromSpatialIndex(streamable)

            verify(exactly = 0) { spatialIndex.remove(any()) }
        }

    }

    @Nested
    inner class OnBoundingBoxChangeTests {

        @BeforeEach
        fun setUp() {
            every { distanceBasedPlayerStreamer.beforeStream(any()) } answers {
                firstArg<() -> Unit>().invoke()
            }
            every { spatialIndex.add(any()) } just Runs
            every { spatialIndex.update(any()) } just Runs
        }

        @Test
        fun shouldUpdateIndexedStreamable() {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
            }
            coordinatesBasedPlayerStreamer.add(streamable)

            coordinatesBasedPlayerStreamer.onBoundingBoxChange(streamable)

            verify { spatialIndex.update(streamable) }
        }

        @Test
        fun shouldNotUpdateNonIndexedStreamable() {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
            }
            coordinatesBasedPlayerStreamer.add(streamable, addToSpatialIndex = false)

            coordinatesBasedPlayerStreamer.onBoundingBoxChange(streamable)

            verify(exactly = 0) { spatialIndex.update(any()) }
        }
    }

    @Nested
    inner class GetStreamInCandidatesTests {

        @BeforeEach
        fun setUp() {
            every { distanceBasedPlayerStreamer.beforeStream(any()) } answers {
                firstArg<() -> Unit>().invoke()
            }
        }

        @Test
        fun shouldReturnStreamInCandidates() {
            val location = locationOf(100f, 200f, 300f, 4, 5)
            val streamLocation = StreamLocation(mockk(), location)
            val streamable1 = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
            }
            val streamable2 = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
            }
            val streamable3 = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
            }
            coordinatesBasedPlayerStreamer.add(streamable3, addToSpatialIndex = false)
            every { spatialIndex.getIntersections(location) } returns listOf(streamable1, streamable2)

            val candidates = coordinatesBasedPlayerStreamer.getStreamInCandidates(streamLocation)

            assertThat(candidates)
                    .containsExactlyInAnyOrder(streamable1, streamable2, streamable3)
        }

        @Test
        fun shouldNotReturnRemovedStreamInCandidate() {
            val location = locationOf(100f, 200f, 300f, 4, 5)
            val streamLocation = StreamLocation(mockk(), location)
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
                every { removeOnDestroyListener(any()) } just Runs
            }
            every { spatialIndex.getIntersections(location) } returns emptyList()
            coordinatesBasedPlayerStreamer.add(streamable, addToSpatialIndex = false)
            coordinatesBasedPlayerStreamer.remove(streamable)

            val candidates = coordinatesBasedPlayerStreamer.getStreamInCandidates(streamLocation)

            assertThat(candidates)
                    .isEmpty()
        }

        @Test
        fun shouldNotReturnDestroyedIndexedStreamInCandidate() {
            val location = locationOf(100f, 200f, 300f, 4, 5)
            val streamLocation = StreamLocation(mockk(), location)
            val streamable = TestStreamable()
            every { spatialIndex.getIntersections(location) } returns emptyList()
            coordinatesBasedPlayerStreamer.add(streamable, addToSpatialIndex = false)
            streamable.destroy()

            val candidates = coordinatesBasedPlayerStreamer.getStreamInCandidates(streamLocation)

            assertThat(candidates)
                    .isEmpty()
        }

        @Test
        fun shouldReturnStreamInCandidateRemovedFromSpatialIndex() {
            val location = locationOf(100f, 200f, 300f, 4, 5)
            val streamLocation = StreamLocation(mockk(), location)
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
                every { removeOnDestroyListener(any()) } just Runs
            }
            spatialIndex.apply {
                every { getIntersections(location) } returns emptyList()
                every { add(any()) } just Runs
                every { remove(any()) } just Runs
            }
            coordinatesBasedPlayerStreamer.add(streamable)
            coordinatesBasedPlayerStreamer.removeFromSpatialIndex(streamable)

            val candidates = coordinatesBasedPlayerStreamer.getStreamInCandidates(streamLocation)

            assertThat(candidates)
                    .containsExactlyInAnyOrder(streamable)
        }

    }

    private class TestStreamable : CoordinatesBasedPlayerStreamable<TestStreamable, Rect3d>() {

        override val priority: Int
            get() = throw UnsupportedOperationException()

        override val streamDistance: Float
            get() = throw UnsupportedOperationException()

        override fun getBoundingBox(): Rect3d = throw UnsupportedOperationException()

        override fun onDestroy() {}

        override fun distanceTo(location: Location): Float = throw UnsupportedOperationException()

        override fun onStreamIn(forPlayer: Player) {}

        override fun onStreamOut(forPlayer: Player) {}

        override fun isStreamedIn(forPlayer: Player): Boolean = throw UnsupportedOperationException()

    }

}