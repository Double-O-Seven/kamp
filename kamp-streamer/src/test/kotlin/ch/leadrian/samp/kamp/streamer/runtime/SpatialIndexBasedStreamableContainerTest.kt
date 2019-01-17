package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.streamer.runtime.entity.SpatialIndexBasedStreamable
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

internal class SpatialIndexBasedStreamableContainerTest {

    private lateinit var spatialIndexBasedStreamer: SpatialIndexBasedStreamableContainer<TestStreamable, Rect3d>

    private val spatialIndex = mockk<SpatialIndex3D<TestStreamable>>()

    @BeforeEach
    fun setUp() {
        spatialIndexBasedStreamer = SpatialIndexBasedStreamableContainer(spatialIndex, TestStreamable::class)
    }

    @Nested
    inner class AddTests {

        @BeforeEach
        fun setUp() {
            every { spatialIndex.add(any()) } just Runs
        }

        @Test
        fun shouldAddStreamableToSpatialIndexWhenOnStreamIsCalled() {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
            }

            spatialIndexBasedStreamer.add(streamable)
            spatialIndexBasedStreamer.onStream()

            verify { spatialIndex.add(streamable) }
        }

        @Test
        fun shouldNotAddStreamableToSpatialIndexIfOnStreamIsNotCalled() {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
            }

            spatialIndexBasedStreamer.add(streamable)

            verify(exactly = 0) { spatialIndex.add(any()) }
        }

        @Test
        fun shouldNotAddNonIndexedStreamableToSpatialIndex() {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
            }

            spatialIndexBasedStreamer.add(streamable, addToSpatialIndex = false)
            spatialIndexBasedStreamer.onStream()

            verify(exactly = 0) { spatialIndex.add(any()) }
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun shouldAddStreamerAsOnDestroyListener(addToSpatialIndex: Boolean) {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
            }

            spatialIndexBasedStreamer.add(streamable, addToSpatialIndex)

            verify { streamable.addOnDestroyListener(spatialIndexBasedStreamer) }
        }

    }

    @Nested
    inner class RemoveTests {

        @BeforeEach
        fun setUp() {
            every { spatialIndex.add(any()) } just Runs
            every { spatialIndex.remove(any()) } just Runs
        }

        @Test
        fun shouldRemoveStreamableFromSpatialIndexWhenOnStreamIsCalled() {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
                every { removeOnDestroyListener(any()) } just Runs
            }
            spatialIndexBasedStreamer.add(streamable)

            spatialIndexBasedStreamer.remove(streamable)
            spatialIndexBasedStreamer.onStream()

            verify { spatialIndex.remove(streamable) }
        }

        @Test
        fun shouldNotRemoveStreamableFromSpatialIndexIfOnStreamIsNotCalled() {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
                every { removeOnDestroyListener(any()) } just Runs
            }
            spatialIndexBasedStreamer.add(streamable)

            spatialIndexBasedStreamer.remove(streamable)

            verify(exactly = 0) { spatialIndex.remove(streamable) }
        }

        @Test
        fun shouldNotRemoveNonIndexedStreamableFromSpatialIndex() {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
                every { removeOnDestroyListener(any()) } just Runs
            }
            spatialIndexBasedStreamer.add(streamable, addToSpatialIndex = false)

            spatialIndexBasedStreamer.remove(streamable)
            spatialIndexBasedStreamer.onStream()

            verify(exactly = 0) { spatialIndex.remove(any()) }
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun shouldRemoveStreamerAsOnDestroyListener(addToSpatialIndex: Boolean) {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
                every { removeOnDestroyListener(any()) } just Runs
            }
            spatialIndexBasedStreamer.add(streamable, addToSpatialIndex)

            spatialIndexBasedStreamer.remove(streamable)
            spatialIndexBasedStreamer.onStream()

            verify { streamable.removeOnDestroyListener(spatialIndexBasedStreamer) }
        }

    }

    @Nested
    inner class RemoveFromSpatialIndexTests {

        @BeforeEach
        fun setUp() {
            every { spatialIndex.add(any()) } just Runs
        }

        @Test
        fun shouldRemoveIndexedStreamableFromSpatialIndexWhenOnStreamIsCalled() {
            every { spatialIndex.remove(any()) } just Runs
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
                every { removeOnDestroyListener(any()) } just Runs
            }
            spatialIndexBasedStreamer.add(streamable)

            spatialIndexBasedStreamer.removeFromSpatialIndex(streamable)
            spatialIndexBasedStreamer.onStream()

            verify { spatialIndex.remove(streamable) }
        }

        @Test
        fun shouldNotRemoveIndexedStreamableFromSpatialIndexIfOnStreamIsNotCalled() {
            every { spatialIndex.remove(any()) } just Runs
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
                every { removeOnDestroyListener(any()) } just Runs
            }
            spatialIndexBasedStreamer.add(streamable)

            spatialIndexBasedStreamer.removeFromSpatialIndex(streamable)

            verify(exactly = 0) { spatialIndex.remove(streamable) }
        }

        @Test
        fun shouldNotRemoveNonIndexedStreamableFromSpatialIndex() {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
                every { removeOnDestroyListener(any()) } just Runs
            }
            spatialIndexBasedStreamer.add(streamable, addToSpatialIndex = false)

            spatialIndexBasedStreamer.removeFromSpatialIndex(streamable)
            spatialIndexBasedStreamer.onStream()

            verify(exactly = 0) { spatialIndex.remove(any()) }
        }

    }

    @Nested
    inner class OnBoundingBoxChangeTests {

        @BeforeEach
        fun setUp() {
            every { spatialIndex.add(any()) } just Runs
            every { spatialIndex.update(any()) } just Runs
        }

        @Test
        fun shouldUpdateIndexedStreamableWhenOnStreamIsCalled() {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
            }
            spatialIndexBasedStreamer.add(streamable)

            spatialIndexBasedStreamer.onBoundingBoxChange(streamable)
            spatialIndexBasedStreamer.onStream()

            verify { spatialIndex.update(streamable) }
        }

        @Test
        fun shouldNotUpdateIndexedStreamableIfOnStreamIsNotCalled() {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
            }
            spatialIndexBasedStreamer.add(streamable)

            spatialIndexBasedStreamer.onBoundingBoxChange(streamable)

            verify(exactly = 0) { spatialIndex.update(streamable) }
        }

        @Test
        fun shouldNotUpdateNonIndexedStreamable() {
            val streamable = mockk<TestStreamable> {
                every { addOnDestroyListener(any()) } just Runs
            }
            spatialIndexBasedStreamer.add(streamable, addToSpatialIndex = false)

            spatialIndexBasedStreamer.onBoundingBoxChange(streamable)
            spatialIndexBasedStreamer.onStream()

            verify(exactly = 0) { spatialIndex.update(any()) }
        }
    }

    @Nested
    inner class GetStreamInCandidatesTests {

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
            spatialIndexBasedStreamer.add(streamable3, addToSpatialIndex = false)
            spatialIndexBasedStreamer.onStream()
            every { spatialIndex.getIntersections(location) } returns listOf(streamable1, streamable2)

            val candidates = spatialIndexBasedStreamer.getStreamInCandidates(streamLocation)

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
            spatialIndexBasedStreamer.add(streamable, addToSpatialIndex = false)
            spatialIndexBasedStreamer.remove(streamable)
            spatialIndexBasedStreamer.onStream()

            val candidates = spatialIndexBasedStreamer.getStreamInCandidates(streamLocation)

            assertThat(candidates)
                    .isEmpty()
        }

        @Test
        fun shouldNotReturnDestroyedIndexedStreamInCandidate() {
            val location = locationOf(100f, 200f, 300f, 4, 5)
            val streamLocation = StreamLocation(mockk(), location)
            val streamable = TestStreamable()
            every { spatialIndex.getIntersections(location) } returns emptyList()
            spatialIndexBasedStreamer.add(streamable, addToSpatialIndex = false)
            streamable.destroy()
            spatialIndexBasedStreamer.onStream()

            val candidates = spatialIndexBasedStreamer.getStreamInCandidates(streamLocation)

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
            spatialIndexBasedStreamer.add(streamable)
            spatialIndexBasedStreamer.removeFromSpatialIndex(streamable)
            spatialIndexBasedStreamer.onStream()

            val candidates = spatialIndexBasedStreamer.getStreamInCandidates(streamLocation)

            assertThat(candidates)
                    .containsExactlyInAnyOrder(streamable)
        }

    }

    private class TestStreamable : SpatialIndexBasedStreamable<TestStreamable, Rect3d>() {

        override val priority: Int
            get() = throw UnsupportedOperationException()

        override fun getBoundingBox(): Rect3d = throw UnsupportedOperationException()

        override fun onDestroy() {}

    }

}